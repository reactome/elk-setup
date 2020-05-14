FROM docker.elastic.co/logstash/logstash:7.4.0
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-prune
COPY ./logstash-filter-usage_type-0.0.1.gem /tmp/logstash-filter-usage_type-0.0.1.gem
COPY ./logstash-filter-usage_type.gemspec /tmp/logstash-filter-usage_type.gemspec
RUN /usr/share/logstash/bin/logstash-plugin install --no-verify --local /tmp/logstash-filter-usage_type-0.0.1.gem
