#!/bin/bash

IPADDR=$(ip a s | sed -ne '/127.0.0.1/!{s/^[ \t]*inet[ \t]*\([0-9.]\+\)\/.*$/\1/p}')

/usr/bin/java -Dswarm.http.port=8080 -Dswarm.bind.address=$IPADDR -jar /opt/booker/booker-pricing.jar
