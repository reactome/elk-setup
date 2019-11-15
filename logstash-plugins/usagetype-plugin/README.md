# usage_type filter plugin

Logstash plugins that are written in Java are built using gradle. For more information on the gradle build process, see: https://guides.gradle.org/building-java-applications/

For more information on writing custom Logstash plugins in Java, see: https://www.elastic.co/guide/en/logstash/7.4/java-filter-plugin.html

To build the usage_type plugin
 - checkout the `logstash` submodule
 - build `logstash`:
```bash
$ cd logstash
$ ./gradlew jar
```
 - then build this project
```bash
$ cd ..
$ ./gradlew assemble
```

To build a gem that can be installed into logstash, execute:
```bash
$ ./gradlew gem
```
