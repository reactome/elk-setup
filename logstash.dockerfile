FROM docker.elastic.co/logstash/logstash-oss:6.1.0
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-prune
