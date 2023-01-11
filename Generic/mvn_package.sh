rm dap-patternsearch-checker-service/target/dap-patternsearch-analysis-service.jar
cd dap-patternsearch-checker-service/
mvn clean package -DskipTests

rm dmp-patternsearch-data-service/target/dmp-patternsearch-data-service.jar
cd ..
cd dmp-patternsearch-data-service/
mvn clean package -DskipTests

rm patternsearch-client-service/target/dap-patternsearch-client-service.jar
cd patternsearch-client-service/
mvn clean package -DskipTests

#cd ..