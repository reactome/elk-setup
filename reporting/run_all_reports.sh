#! /bin/bash

# This script will call run_report.sh to generate and send (by email) several reports.
# Reports will span the time period $FROM_DATE to $TO_DATE
# Arguments:
#  1 - FROM_DATE - the earlier date of the reporting period. Formatted as yyyymmddd. Example: 20181017
#  2 - TO_DATE - the later date of the reporting period. Formatted as yyyymmddd. Example: 20191017
#  3 - MAIL_TO - The email address of the recipient of these reports. A list of emails can be given, if they are separated by a comma only (no spaces).

FROM_DATE=$1
TO_DATE=$2
MAIL_TO=$3

# Use https://jqplay.org/ to test jq filter expressions.

# bash run_report.sh $FROM_DATE $TO_DATE data_xfer_by_month_no_bots.query.json data_xfer_no_bots "Month, Gigabytes transferred" '.aggregations.Month.buckets[] | [ .key_as_string, .Bytes_transferred.value ] | @csv' $MAIL_TO

# bash run_report.sh $FROM_DATE $TO_DATE unique_ips_by_month.query.json ip_counts_by_month "Month, IP Count" '.aggregations.month_agg.buckets[] | [ .key_as_string, .ip_count.value ] | @csv' $MAIL_TO

# bash run_report.sh $FROM_DATE $TO_DATE hits_by_month.query.json hits_by_month "Month, Hits" '.aggregations.month.buckets[] | [ .key_as_string, .doc_count ] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE http_codes_by_month_no_bots.query.json HTTP_Codes_no_bots "Month, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count" '.aggregations.Month.buckets[] | [.key_as_string, (.HTTP_Code_Category.buckets[] | .key, .doc_count)] |@csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE http_codes_by_month_WITH_bots.query.json HTTP_Codes_With_bots "Month, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count, HTTP Code, Count" '.aggregations.Month.buckets[] | [.key_as_string, (.HTTP_Code_Category.buckets[] | .key, .doc_count)] |@csv' $MAIL_TO

# bash run_report.sh $FROM_DATE $TO_DATE usage_types_by_month.json usage_types_by_month "Month, Usage Type, Count" '.aggregations.YearMonth.buckets[] as $x | $x.UsageType.buckets[] | [$x.key_as_string, .key, .doc_count] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE web_requests.query.json web_requsts "Month, Usage Type, Count, Bytes" '.aggregations.month_agg.buckets[] as $x | $x.usage_type_agg.buckets[] | [ $x.key_as_string, .key, .doc_count, .bytes_agg.value ] | @csv' $MAIL_TO

bash run_report.sh $FROM_DATE $TO_DATE unique_hosts.query.json unique_hosts "Month, Usage Type, Unique Client IP Count", '.aggregations.months_agg.buckets[] as $x | $x.usage_type_agg.buckets[] | [ $x.key_as_string , .key, .unique_ip_agg.value] | @csv' $MAIL_TO
