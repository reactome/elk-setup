#! /bin/bash
FROM_DATE=$1
TO_DATE=$2
MAIL_TO=$3

# Use https://jqplay.org/ to test jq filter expressions.

bash run_report.sh $FROM_DATE $TO_DATE data_xfer_by_month_no_bots.query.json data_xfer_no_bots "Month, Gigabytes transferred" '.aggregations.Month.buckets[] | [ .key_as_string, .Bytes_transferred.value ] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE ip_counts_by_month.query.json ip_counts_by_month "Month, IP Count" '.aggregations.month_agg.buckets[] | [ .key_as_string, .ip_count.value ] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE hits_by_month.query.json hits_by_month "Month, Hits" '.aggregations.month.buckets[] | [ .key_as_string, .doc_count ] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE http_codes_by_month_no_bots.query.json HTTP_Codes_no_bots "Month, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count" '.aggregations.Month.buckets[] | [.key_as_string, (.HTTP_Code_Category.buckets[] | .key, .doc_count)] |@csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE http_codes_by_month_WITH_bots.query.json HTTP_Codes_With_bots "Month, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count" '.aggregations.Month.buckets[] | [.key_as_string, (.HTTP_Code_Category.buckets[] | .key, .doc_count)] |@csv' $MAIL_TO
