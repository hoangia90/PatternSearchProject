sudo docker container rm dap-patternsearch-client-service db
sudo docker image rm dap-patternsearch-client-service

#mvn clean package -DskipTests
cp target/dap-patternsearch-client-service.jar src/main/docker/

cd src/main/docker/

sudo docker compose up