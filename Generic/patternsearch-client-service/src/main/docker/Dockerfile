FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} dap-patternsearch-client-service.jar
#ADD target/dap-patternsearch-client-service.jar dap-patternsearch-client-service.jar
ADD dap-patternsearch-client-service.jar dap-patternsearch-client-service.jar
ENTRYPOINT ["java", "-jar", "dap-patternsearch-client-service.jar"]
EXPOSE 9292