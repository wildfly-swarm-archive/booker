#!/bin/bash

cd ${HOME}/apps/hawkular-1.0.0.Alpha5

./bin/standalone.sh -Djboss.socket.binding.port-offset=2000
