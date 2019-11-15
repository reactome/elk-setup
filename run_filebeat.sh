#! /bin/bash

# Note: You will probably need to update the mountpoints for the log files.
# Also: Make sure you `POST _template/reactome_template` the content of reactomews_dynamicmapping.json BEFORE
# trying to import any log files.

docker run --rm --name filebeat -v $(pwd)/filebeat.yml:/usr/share/filebeat/filebeat.yml:rw \
                                -v $(pwd)/extended_log:/logs_for_elk/extended_log \
                                --network=elk_default \
                                docker.elastic.co/beats/filebeat:7.4.0 /bin/bash -c "/usr/local/bin/docker-entrypoint -e"
