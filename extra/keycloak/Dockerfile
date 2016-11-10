# This image provides a customized version of Keycloak with the booker
# realm configured out of the box.

FROM jboss/keycloak:2.1.0.Final

MAINTAINER Ben Browning <bbrownin@redhat.com>

EXPOSE 8080

COPY ./booker.json /tmp/booker.json

RUN chmod -R go+rw $JBOSS_HOME/standalone

CMD ["-b", "0.0.0.0", "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-Dkeycloak.migration.file=/tmp/booker.json"]
