# Before building this dockerfile, remember to build the logstash-filter-usage_type plugin: `cd logstash-plugins/usagetype-plugin && ./gradlew gem && mv *.gem ../../ && mv *.gemspec ../../`
# For more on building logstash plugins, see: https://www.elastic.co/guide/en/logstash/7.x/java-filter-plugin.html#_running_the_gradle_packaging_task_3
FROM docker.elastic.co/logstash/logstash:7.16.3
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-prune
COPY ./logstash-filter-usage_type-0.0.1.gem /tmp/logstash-filter-usage_type-0.0.1.gem
COPY ./logstash-filter-usage_type.gemspec /tmp/logstash-filter-usage_type.gemspec
RUN /usr/share/logstash/bin/logstash-plugin install --no-verify --local /tmp/logstash-filter-usage_type-0.0.1.gem
