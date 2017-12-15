#! /bin/bash

docker run --rm -v $(pwd)/filebeat.yml:/usr/share/filebeat/filebeat.yml \
				-v $(pwd)/logs_for_elk/extended_log:/logs_for_elk/extended_log \
				--network=elk_default \
				docker.elastic.co/beats/filebeat:6.0.0
