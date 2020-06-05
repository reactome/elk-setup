FROM docker.elastic.co/kibana/kibana:7.6.1
RUN kibana-plugin install https://d3g5vo6xdbdb9a.cloudfront.net/downloads/kibana-plugins/opendistro-security/opendistro_security_kibana_plugin-1.7.0.0.zip
