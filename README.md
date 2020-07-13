# elk-setup
Setup to use when processing logs with ElasticSearch, Logstash, &amp; Kibana

Run the elastic server first, to set up security.

You will need to generate certificates, follow these steps:

(See the page on generating certificates: https://opendistro.github.io/for-elasticsearch-docs/docs/security/configuration/generate-certificates/)

 1. These first two commands result in root CA file: root-ca.pem
```bash
openssl genrsa -out root-ca-key.pem 2048
openssl req -new -x509 -sha256 -key root-ca-key.pem -out root-ca.pem
```
 1. Next few commands generate key (admin-key.pem) and certificate (admin.pem)
```bash
openssl genrsa -out admin-key-temp.pem 2048
openssl pkcs8 -inform PEM -outform PEM -in admin-key-temp.pem -topk8 -nocrypt -v1 PBE-SHA1-3DES -out admin-key.pem
openssl req -new -key admin-key.pem -out admin.csr
openssl x509 -req -in admin.csr -CA root-ca.pem -CAkey root-ca-key.pem -CAcreateserial -sha256 -out admin.pem
```

 1. Run `docker-compose up esserver`, because we need to initialise the Open Distro for Elastic security plugin before running the rest of the stack.
 1. Run the security plugin tool:
```bash
docker exec elk_esserver_1 /bin/bash /usr/share/elasticsearch/plugins/opendistro_security/tools/securityadmin.sh -h esserver -p 9300 -cd /usr/share/elasticsearch/plugins/opendistro_security/securityconfig/ -icl -nhnv -cacert /usr/share/elasticsearch/config/root-ca.pem -cert /usr/share/elasticsearch/config/admin.pem -key /usr/share/elasticsearch/config/admin-key.pem
```
 1. Set passwords.  You will need to do this for the main users: admin, kibanaserver, and logstash.
  - In the elastic search container (esserver), there is an _interactive_ `hash.sh` script in `/usr/share/elasticsearch/plugins/opendistro_security/tools` that will generate the hash for a password (other hash tools may not work with OpenDistro). You can use this command:
```bash
docker exec -it elk_esserver_1 /bin/bash /usr/share/elasticsearch/plugins/opendistro_security/tools/hash.sh
```
  - Copy & paste that hash into internal_users.yml. If you add new users, be sure to set them up with a password.


If these steps run without errors, you can proceed to starting the other containers:

 - Run `docker-compose up -d` to run the ELK stack.

Execute `POST _template/reactome_template` with the content of `reactome_dynamicmapping.json`. You can do this from the "Developer Tools" page in Kibana. Do this **_BEFORE_** pushing any data to Elastic. You can do this from the command line with curl, or you can use the dev tools in Kibana.

Run `bash run_filebeat.sh` on whichever machine has the log files you want to send to logstash. The current configuration assumes that logstash and the log files will be on the same machine as the ELK stack, but it doesn't need to be that way. Just edit `filebeat.yml` to send the logs to different machine.

Import saved objects from kibana_saved_ojects:
 - kibana_index_pattern.ndjson - this contains scripted fields, so import this first.
 - kibana_searches.ndjson - other objects might reference searches, so import this file second.
 - kibana_visualizations.ndjson - dashboards contain visualizations, so import this file third.
 - kibana_dashboards.ndjson - import this file last.
