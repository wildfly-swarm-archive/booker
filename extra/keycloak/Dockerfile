# This image provides a customized version of Keycloak with the booker
# realm configured out of the box.

FROM jboss/keycloak

MAINTAINER Ben Browning <bbrownin@redhat.com>

EXPOSE 8080

ENV BOOKER_JSON /tmp/booker.json

COPY ./booker.json $BOOKER_JSON

RUN chmod -R go+rw $JBOSS_HOME

CMD ["/opt/jboss/keycloak/bin/standalone.sh", "-b", "0.0.0.0", "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-Dkeycloak.migration.file=$BOOKER_JSON"]
