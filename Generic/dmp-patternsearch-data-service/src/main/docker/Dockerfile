FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} dmp-patternsearch-data-service.jar
#ADD target/dmp-patternsearch-data-service.jar dmp-patternsearch-data-service.jar
ADD dmp-patternsearch-data-service.jar dmp-patternsearch-data-service.jar
ENTRYPOINT ["java", "-jar", "dmp-patternsearch-data-service.jar"]
EXPOSE 9090