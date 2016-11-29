FROM jboss/base-jdk:8

USER root
RUN yum install -y iproute
ADD launch.sh /usr/bin/launch.sh
RUN chmod +x /usr/bin/launch.sh
RUN mkdir -p /opt/booker
RUN chown jboss:jboss /opt/booker

USER jboss
ADD booker-web-client-swarm.jar /opt/booker/booker-web-client.jar

EXPOSE 8080

ENTRYPOINT /usr/bin/launch.sh
