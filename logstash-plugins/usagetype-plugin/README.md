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


## Usage types

| Usage Type | Description |
| -----------| ------------|
| COM | Commercial |
| ORG |Organization |
| GOV |Government |
| MIL |Military |
| EDU |University/College/School |
| LIB | Library |
| CDN |Content Delivery Network |
| ISP |Fixed Line ISP |
| MOB |Mobile ISP |
| DCH |Data Center/Web Hosting/Transit |
| SES | Search Engine Spider |
| RSV |Reserved |

(From https://blog.ip2location.com/knowledge-base/what-is-usage-type/ )
