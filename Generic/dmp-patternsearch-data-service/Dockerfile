FROM adoptopenjdk:11-jre-hotspot
ADD target/dmp-patternsearch-data-service.jar dmp-patternsearch-data-service.jar
ENTRYPOINT ["java", "-jar", "dmp-patternsearch-data-service.jar"]
EXPOSE 9090