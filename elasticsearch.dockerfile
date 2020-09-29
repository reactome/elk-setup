FROM docker.elastic.co/elasticsearch/elasticsearch:7.7.0
# Install the security plugin
RUN elasticsearch-plugin install --batch https://d3g5vo6xdbdb9a.cloudfront.net/downloads/elasticsearch-plugins/opendistro-security/opendistro_security-1.8.0.0.zip
# Update the config: XPack security is explicity disabled (you can only have OpenDistro security or XPack security enabled - I don't think it's possible to have both)
RUN echo "xpack.security.enabled: false" >> /usr/share/elasticsearch/config/elasticsearch.yml
# Set JAVA_HOME - the security initialisation script needs this.
ENV JAVA_HOME /usr/share/elasticsearch/jdk
