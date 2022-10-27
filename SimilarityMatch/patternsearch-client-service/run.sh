#!/bin/sh
NAME=dmp-mem-service
dt=$(date '+%Y_%m_%d_%Hh_%Mm_%Ss');
LOG_FILE="${dt}-${NAME}.log"
touch ${LOG_FILE}

echo --------------------------------------------------------------------
echo Starting dmp metadata service using the default parameter defined in application.yml
echo --------------------------------------------------------------------
java -Xms512m -Xmx2048m -Djava.net.preferIPv4Stack=true -jar ${NAME}.jar >> ${LOG_FILE}
