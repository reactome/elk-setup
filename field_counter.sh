#! /bin/bash

# This is a small utility script that will search for all "kvXtrct" fields (Extracted from URL parameters, as key-value pairs) and then
# output how many occurrences there are of each field. The purpose is there were too many kvXtrct fields that had very little value, so
# this helps decide which to keep and which to remove.
# NOTE: this script requires jq.

USER=$1
PASSWORD=$2

# Find all key-value fields that were extracted form URL strings, and then dump them to a file. They all get the prefix "kvXtrct" from the logstash pipeline.
curl -s -u $USER:$PASSWORD --insecure -X GET https://localhost:9200/reactome-*/_mapping/field/kvX*  | jq '. | to_entries | .[].value.mappings | to_entries | .[].key' | sort -u > kvXtrctFields

echo "checking counts for $(wc -l kvXtrctFields) fields"
cat kvXtrctFields | while read line
do
  # Now check specific field on the line
  count=$(curl -s -u $USER:$PASSWORD --insecure  -H 'Content-Type: application/json' -X POST https://localhost:9200/_search -d '
  {
    "size": 0,
    "query": {
        "exists" : { "field" : '$line' }
    }
  }' | jq '.hits.total.value')
  echo -e "$line\t$count" >> field_counts.txt
done

# If you want to check a single field to see how many times it is used:
# GET /_search
# {
#     "size": 0,
#     "query": {
#         "exists" : { "field" : "kvXtrct_some_field" }
#     }
# }
#
# If you want to get rid of a specific field in any documents that contain it:
# POST reactome-main-*/_update_by_query?conflicts=proceed
# {
#     "script" : "ctx._source.remove('kvXtrct_some_field')",
#     "query" : {
#         "exists": { "field": "kvXtrct_some_field" }
#     }
# }
