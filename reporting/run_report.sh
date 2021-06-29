#! /bin/bash

# Runs a report in elasticsearch, processes the result into a CSV, and then sends it to an email address.
# Arguments:
#  1 - FROM_DATE - the beginning of the reporting period, formatted as yyyymmdd. Example: 20181017
#  2 - TO_DATE - the end of the reporting period, formatted as yyyymmdd. Example: 20191017
#  3 - QUERY_FILE - the path to the file containing the query for elasticsearch.
#      This file should contain the placeholder strings _FROM_DATE and _TO_DATE so that this
#      script can replace them with the user-provided values.
#  4 - OUTPUT_FILE_PREFIX - output files will be named as ${OUTPUT_FILE_PREFIX}_${FROM_DATE}_to_${TO_DATE}.csv
#  5 - HEADER - the header that will appear at the top of the file.
#  6 - JQ_FILTER - This is a string that will be fed to jq to process the JSON results from elastic search into CSV-formatted data.
#      This should be in single-quotes.
#  7 - MAIL_TO - email address(es) that the reports will be sent to. You can specify multiple addresses if
#      you need to, by separating them with a comma (no spaces). mutt is used to send email.
# Requirements:
#  This script assumes that elasticsearch is running locally. It requires curl, mutt, and jq. mutt may require the gpgsm package.

FROM_DATE=$1
TO_DATE=$2
QUERY_FILE=$3
OUTPUT_FILE_PREFIX=$4
HEADER=$5
JQ_FILTER=$6
MAIL_TO=$7
set -e
# The query file should have placeholder so that we can specify the date range.
QUERY=$(sed -e "s/_FROM_DATE/\"$FROM_DATE\"/g" <  $QUERY_FILE | sed -e "s/_TO_DATE/\"$TO_DATE\"/g")
FILE_NAME=${OUTPUT_FILE_PREFIX}_${FROM_DATE}_to_${TO_DATE}.csv
# write the file header.
echo "$HEADER" > $FILE_NAME
# Query elasticsearch. You will need to have jq installed to transform elasticsearch's JSON output into a CSV.
# Also: Make sure you store your elastic credentials in a .netrc files in the directory where run_report.sh (this script) lives
curl --netrc-file ./.netrc --insecure -s -H 'Content-Type: application/json'  -XGET https://localhost:9200/reactome-main*/_search/ -d "$QUERY" | jq -r "$JQ_FILTER" >> $FILE_NAME
# send the report. If mutt gives you the error "GPGME: CMS protocol not available" you may need to install the gpgsm package.
mutt -s "Report from ELK: $FILE_NAME" -a $FILE_NAME -- $MAIL_TO  <<< "Report $FILE_NAME is attached."
set +e
