#!/bin/bash

echo "***************************"
echo "*** Store Microservice ****"
echo "***************************"

cd ${HOME}/booker/store

if [ "$1" != "" ];
then
        PORT="-Djboss.socket.binding.port-offset=${1}"
fi

HAWKULAR="-Dswarm.hawkular.user=demo \
	-Dswarm.hawkular.password=Demo-123 \
	-Dswarm.hawkular.host=localhost \
	-Dswarm.hawkular.port=10080"

java ${PORT} ${HAWKULAR} -jar target/*swarm.jar

#mvn wildfly-swarm:run
