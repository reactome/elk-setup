# elk-setup
Setup to use when processing logs with ElasticSearch, Logstash, &amp; Kibana

Run `docker-compose up -d` to run the ELK stack.

Run `bash run_filebeat.sh` on whichever machine has the log files you want to send to logstash. The current configuration assumes that logstash and the log files will be on the same machine as the ELK stack, but it doesn't need to be that way. Just edit `filebeat.yml` to send the logs to different machine.

`saved_searchs.json` and `saved_visualizations.json` are the JSON representations of searches and visualizations. You can import them into Kibana, once Kibana is up and running. Be sure to name your index "REACTOME_INDEX", as that's what these searches and visualizations expect.
