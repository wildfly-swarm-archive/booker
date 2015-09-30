#!/bin/bash

echo "*****************************"
echo "*** Library Microservice ****"
echo "*****************************"

cd ${HOME}/booker/library

if [ "$1" != "" ];
then
        PORT="-Djboss.socket.binding.port-offset=${1}"
fi

java ${PORT} -jar target/*-swarm.jar

#mvn wildfly-swarm:run
