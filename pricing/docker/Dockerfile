FROM jboss/base-jdk:8

USER root
RUN yum install -y iproute
ADD launch.sh /usr/bin/launch.sh
RUN chmod +x /usr/bin/launch.sh
RUN mkdir -p /opt/booker
RUN chown jboss:jboss /opt/booker

USER jboss
ADD booker-pricing-swarm.jar /opt/booker/booker-pricing.jar

ENTRYPOINT /usr/bin/launch.sh
