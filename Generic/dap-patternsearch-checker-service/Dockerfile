FROM adoptopenjdk:11-jre-hotspot
ADD target/dap-patternsearch-analysis-service.jar dap-patternsearch-analysis-service.jar
ENTRYPOINT ["java", "-jar", "dap-patternsearch-analysis-service.jar"]
EXPOSE 9191