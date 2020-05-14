# elk-setup
Setup to use when processing logs with ElasticSearch, Logstash, &amp; Kibana

Run `docker-compose up -d` to run the ELK stack.

Execute `POST _template/reactome_template` with the content of `reactome_dynamicmapping.json`. You can do this from the "Developer Tools" page in Kibana. Do this _BEFORE_ pushing any data to Elastic.

Run `bash run_filebeat.sh` on whichever machine has the log files you want to send to logstash. The current configuration assumes that logstash and the log files will be on the same machine as the ELK stack, but it doesn't need to be that way. Just edit `filebeat.yml` to send the logs to different machine.

Import saved objects from kibana_saved_ojects:
 - kibana_index_pattern.ndjson - this contains scripted fields, so import this first.
 - kibana_searches.ndjson - other objects might reference searches, so import this file second.
 - kibana_visualizations.ndjson - dashboards contain visualizations, so import this file third.
 - kibana_dashboards.ndjson - import this file last.
