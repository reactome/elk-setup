FROM docker.elastic.co/kibana/kibana:7.7.0
RUN kibana-plugin install https://d3g5vo6xdbdb9a.cloudfront.net/downloads/kibana-plugins/opendistro-security/opendistro_security_kibana_plugin-1.8.0.0.zip
