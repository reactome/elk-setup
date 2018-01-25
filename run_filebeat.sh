#! /bin/bash

docker run --rm --name filebeat -v $(pwd)/filebeat.yml:/usr/share/filebeat/filebeat.yml:rw \
                                -v $(pwd)/reactomews.dynamicmapping.json:/reactomews.dynamicmapping.json \
                                -v /datastore/logs_for_elk/extended_log:/logs_for_elk/2016_extended_log \
                                --network=elksetup_default \
                                docker.elastic.co/beats/filebeat:6.1.1 /bin/bash -c "curl -XPUT 'esserver:9200/reactome-main?pretty' -H 'Content-Type: application/json' -d@/reactomews.dynamicmapping.json && curl -XPUT 'esserver:9200/reactomews?pretty' -H 'Content-Type: application/json' -d@/reactomews.dynamicmapping.json && /usr/local/bin/docker-entrypoint -e"
