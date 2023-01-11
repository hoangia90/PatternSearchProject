FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} dap-patternsearch-analysis-service.jar
#ADD target/dap-patternsearch-analysis-service.jar dap-patternsearch-analysis-service.jar
ADD dap-patternsearch-analysis-service.jar dap-patternsearch-analysis-service.jar
ENTRYPOINT ["java", "-jar", "dap-patternsearch-analysis-service.jar"]
EXPOSE 9191