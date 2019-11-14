FROM docker.elastic.co/logstash/logstash:7.4.0
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-prune
