#!/bin/bash

echo "*****************************"
echo "*** Pricing Microservice ****"
echo "*****************************"

cd ${HOME}/booker/pricing
if [ "$1" != "" ];
then
        PORT="-Djboss.socket.binding.port-offset=${1}"
fi

java ${PORT} -jar target/*-swarm.jar

#mvn wildfly-swarm:run
