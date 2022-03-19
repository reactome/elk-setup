#!/bin/bash
set -e

#################################################################################
# USAGE
#
# This script works by first checking for files that end in .indexed in the log folder for the specified server. It then marks the corresponding file in s3 to be excluded from a sync to the log folder. 
# Once the files are in the log folder they are decompressed. They are then given a name based on what LogStash is looking for. Once the files are transfered Logstash will automatically create corresponding indexes
# In order to reprocess a particular file just remove the corrsponding index file and then rerun the script
#
#################################################################################


# Export the path so when called by cron, the needed binaries (e.g. the aws cli) can be found
export PATH="/usr/local/bin:/usr/bin:/bin:/usr/local/games:/usr/games:/snap/bin:/usr/lib/jvm/java-8-oracle/bin:/usr/lib/jvm/java-8-oracle/db/bin:/usr/lib/jvm/java-8-oracle/jre/bin"

echo ""
echo "starting process"
date
current_year=$(date +%Y)
echo "Processing for year: $current_year"
echo ""

elk_log_path="/opt/gitroot/elk-setup/logs_for_elk/"
echo "Elk log path: $elk_log_path"


process_logs() {
    local domain=${1}
    echo ""
    echo ""
    echo "Processesing logs for: $domain"

    logs_path="/home/s3bot/logs/$domain/"
    echo "Logs path: $logs_path"

    if [ $domain = 'reactome.org' ]
    then
        s3_path="s3://reactome/private/logs/apache/extended_log/"
    elif [ $domain = "idg.reactome.org" ]
    then
        s3_path="s3://reactome/private/logs/idg-apache/extended_log/"
    elif [ $domain = "reactomews" ]
    then
        s3_path="s3://reactome/private/logs/cpws-server/tomcat7_access_logs/"
    fi

    sync_cmd="aws s3 sync $s3_path $logs_path"
    exclude_statements=""
    while read fullpath
    do
       fbname=$(basename "$fullpath" .indexed)
       [ -z "$fbname" ] || exclude_statements+=" --exclude $fbname"
    done <<< "$(ls -A1 $logs_path* | egrep "*.indexed$")"
    sync_cmd_full="$sync_cmd $exclude_statements"
    echo "Executing Sync Command: $sync_cmd_full"
    $sync_cmd_full

    echo "Unzipping new gz files"
    count_gz=`ls -1 $logs_path*.gz 2>/dev/null | wc -l`
    if [ $count_gz != 0 ]
    then
        echo "    - $count_gz files found"
        if [ $domain = "reactomews" ]
        then
            while read filename
            do
                echo "$filename"
                echo "$logs_path/reactomews/$filename"
                new_log_filename=${filename::-7}
                echo "$new_log_filename"
                tar -I pigz -xvf "$logs_path$filename" -C $logs_path\tmp/
                cat $logs_path\tmp/* > $logs_path$new_log_filename
                rm $logs_path\tmp/*
                rm $logs_path$filename
            done <<< "$(ls -A1 $logs_path | egrep "*.tar.gz$")"
        else
            unpigz $logs_path*.gz
        fi
    else
        echo "    - No file with extension 'gz' found"
    fi

    echo ""
    echo "Moving files to $elk_log_path"
    echo "$logs_path"

    while read filename
    do
        if [ -z "$filename" ]; then
            echo "    - No files to move"
        else
            echo "    - Moving $filename"

            file_date=${filename: -7}
            echo 
            echo "filedate: $file_date"

            if [ $domain = "reactome.org" ]
            then
                new_filename="extended_log.main.$file_date"
            elif [ $domain = "idg.reactome.org" ]
            then
                new_filename="extended_log.idg.$file_date"
            else
                new_filename="reactomews/cpws_tomcat_7.reactomews.$file_date.txt"
            fi

            mv_cmd="mv $logs_path$filename $elk_log_path$new_filename"
            echo "$mv_cmd"
            $mv_cmd
            if [ $domain = "reactomews" ]
            then
                indexed_filename="$filename.tar.gz.indexed"
            else
                indexed_filename="$filename.gz.indexed"
            fi
            create_file_cmd="touch $logs_path$indexed_filename"
            echo "    - Creating indexed file: $indexed_filename"
            $create_file_cmd
        fi
    done <<< "$(cd $logs_path && ls -1 --ignore='*.gz.indexed' --ignore='tmp')"
}


process_logs "reactome.org"
process_logs "idg.reactome.org"
process_logs "reactomews"
~                            
