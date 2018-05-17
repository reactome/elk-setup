FROM docker.elastic.co/logstash/logstash-oss:6.2.4
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-prune
