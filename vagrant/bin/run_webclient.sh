#!/bin/bash

echo "********************************"
echo "*** Web-Client Microservice ****"
echo "********************************"

cd ${HOME}/booker/web-client

if [ "$1" != "" ];
then
        PORT="-Dswarm.port.offset=${1}"
fi

java ${PORT} -jar target/*-swarm.jar

#mvn wildfly-swarm:run
