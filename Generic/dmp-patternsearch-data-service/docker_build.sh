sudo docker container rm dmp-patternsearch-data-service db
sudo docker image rm dmp-patternsearch-data-service

#mvn clean package -DskipTests
cp target/dmp-patternsearch-data-service.jar src/main/docker/

cd src/main/docker/

sudo docker compose up