#!/bin/bash

cd ${HOME}/apps/keycloak-1.5.0.Final

./bin/standalone.sh -Djboss.http.port=9090
