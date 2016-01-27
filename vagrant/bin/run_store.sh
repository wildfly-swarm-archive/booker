#!/bin/bash

echo "***************************"
echo "*** Store Microservice ****"
echo "***************************"

cd ${HOME}/booker/store

if [ "$1" != "" ];
then
        PORT="-Dswarm.port.offset=${1}"
fi

java ${PORT} -jar target/*swarm.jar

#mvn wildfly-swarm:run
