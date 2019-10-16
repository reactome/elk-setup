#! /bin/bash
FROM_DATE=$1
TO_DATE=$2
MAIL_TO=$3
# The query file should have placeholder so that we can specify the date range.
QUERY=$(sed -e "s/_FROM_DATE/$FROM_DATE/g" < query.json | sed -e "s/_TO_DATE/$TO_DATE/g")
FILE_NAME=ip_counts_${FROM_DATE}_to_${TO_DATE}.csv
# write the file header.
echo "Month, IP Count" > $FILE_NAME
# Query elasticsearch. You will need to have jq installed to transform elasticsearch's JSON output into a CSV.
curl -s -H 'Content-Type: application/json'  -XGET http://localhost:9200/reactome-main*/_search/ -d "$QUERY" \
        | jq -r '.aggregations.month_agg.buckets[] | [ .key_as_string, .ip_count.value ] | @csv' >> $FILE_NAME
# send the report.
mutt -s "Report from ELK: $FILE_NAME" -a $FILE_NAME -- $MAIL_TO  <<< "Report $FILE_NAME is attached."
