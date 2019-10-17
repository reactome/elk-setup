#! /bin/bash

FROM_DATE=$1
TO_DATE=$2
QUERY_FILE=$3
OUTPUT_FILE_PREFIX=$4
HEADER=$5
JQ_FILTER=$6
MAIL_TO=$7
set -e
# The query file should have placeholder so that we can specify the date range.
QUERY=$(sed -e "s/_FROM_DATE/$FROM_DATE/g" <  $QUERY_FILE | sed -e "s/_TO_DATE/$TO_DATE/g")
FILE_NAME=$OUTPUT_FILE_PREFIX_${FROM_DATE}_to_${TO_DATE}.csv
# write the file header.
echo "$HEADER" > $FILE_NAME
# Query elasticsearch. You will need to have jq installed to transform elasticsearch's JSON output into a CSV.
curl -s -H 'Content-Type: application/json'  -XGET http://localhost:9200/reactome-main*/_search/ -d "$QUERY" \
        | jq -r $JQ_FILTER >> $FILE_NAME
# send the report. If mutt gives you the error "GPGME: CMS protocol not available" you may need to install the gpgsm package.
mutt -s "Report from ELK: $FILE_NAME" -a $FILE_NAME -- $MAIL_TO  <<< "Report $FILE_NAME is attached."
set +e
