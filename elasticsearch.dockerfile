FROM docker.elastic.co/elasticsearch/elasticsearch:7.7.0
RUN elasticsearch-plugin install --batch https://d3g5vo6xdbdb9a.cloudfront.net/downloads/elasticsearch-plugins/opendistro-security/opendistro_security-1.8.0.0.zip
# RUN bash /usr/share/elasticsearch/plugins/opendistro_security/tools/install_demo_configuration.sh -y -i
RUN echo "xpack.security.enabled: false" >> /usr/share/elasticsearch/config/elasticsearch.yml
ENV JAVA_HOME /usr/share/elasticsearch/jdk
