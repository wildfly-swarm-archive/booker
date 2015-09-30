#!/bin/bash

echo "***************************"
echo "*** Store Microservice ****"
echo "***************************"

cd ${HOME}/booker/store

if [ "$1" != "" ];
then
        PORT="-Djboss.socket.binding.port-offset=${1}"
fi

java ${PORT} -jar target/*swarm.jar

#mvn wildfly-swarm:run
