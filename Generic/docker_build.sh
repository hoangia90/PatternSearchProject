sudo docker container rm dmp-patternsearch-data-service dap-patternsearch-analysis-service dap-patternsearch-client-service db
sudo docker image rm dmp-patternsearch-data-service dap-patternsearch-analysis-service dap-patternsearch-client-service db

#mvn clean package -DskipTests
# cp dap-patternsearch-checker-service/target/dap-patternsearch-analysis-service.jar .
# cp dmp-patternsearch-data-service/target/dmp-patternsearch-data-service.jar .
# cp patternsearch-client-service/target/dap-patternsearch-client-service.jar .
# cp target/dap-patternsearch-analysis-service.jar src/main/docker/
# cp target/dmp-patternsearch-data-service.jar src/main/docker/
# cp target/dap-patternsearch-client-service.jar src/main/docker/

# cd src/main/docker/

sudo docker compose up

