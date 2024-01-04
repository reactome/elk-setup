#! /bin/bash

# This script will call run_report.sh to generate and send (by email) several reports.
# Reports will span the time period $FROM_DATE to $TO_DATE
# Arguments:
#  1 - FROM_DATE - the earlier date of the reporting period. Formatted as yyyymmddd. Example: 20181017
#  2 - TO_DATE - the later date of the reporting period. Formatted as yyyymmddd. Example: 20191017
#  3 - MAIL_TO - The email address of the recipient of these reports. A list of emails can be given, if they are separated by a comma only (no spaces).

# you can probably run this from cron with an expression like this: run_all_reports.sh $(date --date="$(date +%Y-%m-01) -1 month" +%Y-%m-%d) $(date --date="$(date +%Y-%m-01) -1 day" +%Y-%m-%d) solomon.shorser@oicr.on.ca

FROM_DATE=$1
TO_DATE=$2
MAIL_TO=$3

# Use https://jqplay.org/ to test jq filter expressions.

bash run_report.sh $FROM_DATE $TO_DATE http_code_counts_by_month_no_bots.query.json HTTP_Codes_no_bots "Month, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count" '.aggregations.Month.buckets[] | [.key_as_string, (.HTTP_Code_Category.buckets[] | .key, .doc_count)] |@csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE http_code_counts_by_month_WITH_bots.query.json HTTP_Codes_With_bots "Month, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count" '.aggregations.Month.buckets[] | [.key_as_string, (.HTTP_Code_Category.buckets[] | .key, .doc_count)] |@csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE web_requests.query.json web_requests "Month, Usage Type, Count, Bytes" '.aggregations.month_agg.buckets[] as $x | $x.usage_type_agg.buckets[] | [ $x.key_as_string, .key, .doc_count, .bytes_agg.value ] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE unique_hosts.query.json unique_hosts "Month, Usage Type, Unique Client IP Count", '.aggregations.months_agg.buckets[] as $x | $x.usage_type_agg.buckets[] | [ $x.key_as_string , .key, .unique_ip_agg.value] | @csv' $MAIL_TO
