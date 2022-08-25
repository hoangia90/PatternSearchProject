#!/bin/bash

STOP_SERVICE=$1

if [ -z "$STOP_SERVICE" ] ;
then    
        echo "ERROR : Please define STOP_SERVICE in parameter"
        echo "ERROR : STOP_SERVICE : the service to stop"
        exit $E_ARG_ERR
fi

pids=( $(ps -ef | grep ${STOP_SERVICE} | grep -v 'grep' | awk -F" " '{ print $2}') )
echo ${pids[@]}

for i in "${pids[@]}"
do
        kill $i
done
