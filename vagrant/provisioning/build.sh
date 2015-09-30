#!/bin/bash

. /vagrant/provisioning/common.sh

cd ${DEMO_HOME}
echo "Cloning Booker Project"
git clone https://github.com/wildfly-swarm/booker.git

cd ${BOOKER_HOME}

# Enable logstash by removing comment block in pom.xml
sed -i 's/<!--//g; s/-->//g' ${BOOKER_HOME}/pom.xml

echo "Building Booker maven project. Could take a while. Downloading the Internet."
${MAVEN_HOME}/bin/mvn -q clean install
