FROM adoptopenjdk:11-jre-hotspot
ADD target/dap-patternsearch-client-service.jar dap-patternsearch-client-service.jar
ENTRYPOINT ["java", "-jar", "dap-patternsearch-client-service.jar"]
EXPOSE 9292