#!/bin/bash -x

. /vagrant/provisioning/common.sh

SOURCE_BINARIES=/vagrant/provisioning/downloads
export SOURCE_BINARIES

##################################################################
# User "demo", password "demo"
# To change password, use "openssl passwd -crypt my_password_here"
##################################################################

add_demo_user()
{
	useradd \
		-d ${DEMO_HOME} \
		-c "Demo" \
		-G users,wheel \
		-p UbxzHpHunqNPk \
		-m \
		demo

	mkdir -p ${DOWNLOAD_HOME} \
	      -p ${APP_HOME} \
	      -p ${BIN_DIR}

	cd ${DEMO_HOME}

	# Copy over the run* scripts to run the various services
	cp ${VAGRANT_HOME}/bin/* ${DEMO_HOME}/bin
	chmod 755 ${DEMO_HOME}/bin/*
}

######################################################
# Download the docker packages to run demo using
# docker containers (not implemented yet)
######################################################

setup_docker()
{
	curl -sSL https://get.docker.com/ | sh
	usermod -aG docker demo
	service docker start
	systemctl docker enable
	docker pull fedora:22
}

######################################################
# Download required 3rd party packages
######################################################

download_required_packages()
{
	if [ -f ${SOURCE_BINARIES}/keycloak-1.5.0.Final.zip ];
	then
		echo "Copying Keycloack"
		cp ${SOURCE_BINARIES}/keycloak-1.5.0.Final.zip ${DOWNLOAD_HOME}
        else
		echo "Downloading Keycloack"
		wget http://downloads.jboss.org/keycloak/1.5.0.Final/keycloak-1.5.0.Final.zip \
			-q -P ${DOWNLOAD_HOME}
        fi

	if [ -f ${SOURCE_BINARIES}/logstash-1.5.4.zip ];
	then
		echo "Copying LogStash"
		cp ${SOURCE_BINARIES}/logstash-1.5.4.zip ${DOWNLOAD_HOME}
        else
		echo "Downloading LogStash"
		wget https://download.elastic.co/logstash/logstash/logstash-1.5.4.zip \
			-q -P ${DOWNLOAD_HOME}
	fi

	if [ -f ${SOURCE_BINARIES}/kibana-4.1.2-linux-x64.tar.gz ];
	then
		echo "Copying Kibana"
		cp ${SOURCE_BINARIES}/kibana-4.1.2-linux-x64.tar.gz ${DOWNLOAD_HOME}
        else
		echo "Downloading Kibana"
		wget https://download.elastic.co/kibana/kibana/kibana-4.1.2-linux-x64.tar.gz \
			-q -P ${DOWNLOAD_HOME}
	fi

	if [ -f ${SOURCE_BINARIES}/apache-maven-3.3.3-bin.zip ];
	then
		echo "Copying Maven"
		cp ${SOURCE_BINARIES}/apache-maven-3.3.3-bin.zip ${DOWNLOAD_HOME}
        else
		echo "Downloading Maven"
		wget http://mirror.olnevhost.net/pub/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.zip \
			-q -P ${DOWNLOAD_HOME}
	fi

	echo "Download JBoss Dev Studio"
	if [ -f ${SOURCE_BINARIES}/jboss-devstudio-8.1.0.GA-installer-standalone.jar ];
	then
		cp ${SOURCE_BINARIES}/jboss-devstudio-8.1.0.GA-installer-standalone.jar ${DOWNLOAD_HOME}
#        else
#		wget http://www.jboss.org/download-manager/file/jboss-devstudio-8.1.0.GA-installer-standalone.jar \
#			-q -P ${DOWNLOAD_HOME}
	fi
}

######################################################
# Unpackage required 3rd party packages
######################################################

unpackage_third_party_apps()
{
	cd ${APP_HOME}

	echo "Uncompressing Keycloak"
	tar zxf ${DOWNLOAD_HOME}/kibana-4.1.2-linux-x64.tar.gz

	echo "Unzipping Keycloak"
	unzip -q ${DOWNLOAD_HOME}/keycloak-1.5.0.Final.zip

	echo "Unzipping LogStash"
	unzip -q ${DOWNLOAD_HOME}/logstash-1.5.4.zip

	echo "Unzipping Maven"
	unzip -q ${DOWNLOAD_HOME}/apache-maven-3.3.3-bin.zip
}

######################################################
# Build Booker app
######################################################

build_booker_app()
{
	if [ -d ${SOURCE_BINARIES}/.m2 ];
	then
		echo "Copying pre-populated maven repository"
		cp -r ${SOURCE_BINARIES}/.m2 ${DEMO_HOME}
	fi

	sudo chown -R demo:users ${DEMO_HOME}

	echo "Building the booker application using Maven"
	chmod 755 /vagrant/provisioning/build.sh
	sudo runuser -l demo -g users /vagrant/provisioning/build.sh
}

setup_demo_shell()
{
cat <<EOL >> ${DEMO_HOME}/.bashrc

PATH=\${PATH}:${APP_HOME}/apache-maven-3.3.3/bin
PATH=\${PATH}:${DEMO_HOME}/bin
PATH=\${PATH}:${DEMO_HOME}/apps/jbdevstudio/studio

set -o vi
EOL
}

##########################################################
# Setting up keycloak by pre-populating a booker realm
# Kludgy by running keycloak, pre-populating the realm
# and then killing the process after 30 seconds. Timing
# should be good enough unless host is slow or
# paging a lot.
##########################################################

setup_keycloak()
{
	echo "Setting up keycloak by pre-populating a booker realm"
	cd ${APP_HOME}/key*
	bin/standalone.sh \
		-Djboss.http.port=9090 \
		-Dkeycloak.migration.action=import \
		-Dkeycloak.migration.provider=singleFile \
		-Dkeycloak.migration.file=${DEMO_HOME}/booker/extra/keycloak/booker.json&
	sleep 30
	sudo pkill -f keycloak
	sudo chown -R demo:users ${APP_HOME}/key*
}

install_jdev_studio() {
	if [ -f ${SOURCE_BINARIES}/jboss-devstudio-8.1.0.GA-installer-standalone.jar ];
	then
		java -jar ${SOURCE_BINARIES}/jboss-devstudio-8.1.0.GA-installer-standalone.jar \
			${SOURCE_BINARIES}/InstallConfigRecord.xml
	fi
}

##############################################
# main()
##############################################

add_demo_user

# This happens after build_booker_app logs in as demo and creates .bashrc
setup_demo_shell   

# Docker support not implemented yet
#setup_docker
download_required_packages
unpackage_third_party_apps
build_booker_app
setup_keycloak

install_jdev_studio

# The End

