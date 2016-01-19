# Multicast

On OSX, you'll probably need to enable multicast on localhost:

    sudo route add -net 230.0.0.0/4 127.0.0.1

# Components

## `web-client`

Simple, normal `.war` deployment that serves the React.js-based
single-page-app, along with an async servlet to power the
Server-Sent-Events (SSE) for Ribbon service discovery.

The React.js components communicate directly with the `store`
and `library` services.

Since this application includes a `main()` method, and creates
a WAR file instead of a JAR file, you'll need to start this
application using the `java -jar` command.

    $ java -jar target/booker-web-client-swarm.jar

##  `store`

Book inventory (pulled from Project Gutenberg) served from
a JAX-RS resource from a CDI-injected service.  Uses the
`pricing` service via Ribbon to determine the price of each
item in the store.

Start this using the maven command.

    $ mvn wildfly-swarm:run

## `pricing`

Simple pricing service that indicates everything is $10 if
you're browsing anonymously, or $9 if you're logged in.

Start this using the maven command.

    $ mvn wildfly-swarm:run

## `library`

Tracks which items are bought by a user using JPA (via an h2
database) from a JAX-RS resource.  Communicates with the `store`
service to associate details with a given book ID.

Start this using the maven command.

$ mvn wildfly-swarm:run

## `keycloak`

The application is built assuming that there is a running keycloak.
Learn more about keycloak and download it at http://keycloak.jboss.org/

Once you have keycloak, start it using the `standalone.sh` command.

    ./bin/standalone.sh -Djboss.http.port=9090

The default user name and password when you start keycloak for the first
time is admin/admin. Use this to access the keycloak console, and then
import the JSON data from this repository in `extra/keycloak`.

# `Vagrant`
A Vagrantfile and support scripts to install and run booker in a
virtual machine. Requires Virtualbox and Vagrant be installed.

# `OpenShift`

Booker on OpenShift 3.x is still a work in progress, but here are the
steps to try it out. These assume you have a working OpenShift 3
environment already setup and configured.

First, build the WildFly Swarm source to image container:

    oc new-project swarm
    oc create -f https://raw.githubusercontent.com/wildfly-swarm/sti-wildflyswarm/master/1.0/test/imagestream.json
    oc create -f https://raw.githubusercontent.com/wildfly-swarm/sti-wildflyswarm/master/1.0/test/build-config.json
    oc start-build wildflyswarm-10-centos7-build

Wait for the WildFly Swarm image to finish building (use `oc status` to
check the progress). Once it has finished, we can start deploying
Booker using that image.

    oc policy add-role-to-user -z default view
    oc new-app --name=booker-keycloak --context-dir=extra/keycloak https://github.com/wildfly-swarm/booker
    oc expose service booker-keycloak
    oc new-app --env="SWARM_JAR=web-client/target/*-swarm.jar" --name=booker-web wildflyswarm-10-centos7~https://github.com/wildfly-swarm/booker
    oc expose service booker-web
    oc new-app --env="SWARM_JAR=library/target/*-swarm.jar" --name=booker-library wildflyswarm-10-centos7~https://github.com/wildfly-swarm/booker
    oc expose service booker-library
    oc new-app --env="SWARM_JAR=store/target/*-swarm.jar" --name=booker-store wildflyswarm-10-centos7~https://github.com/wildfly-swarm/booker
    oc expose service booker-store
    oc new-app --env="SWARM_JAR=pricing/target/*-swarm.jar" --name=booker-pricing wildflyswarm-10-centos7~https://github.com/wildfly-swarm/booker
    oc expose service booker-pricing

After the `booker-web` application deploys, use `oc get routes` to
find its exposed hostname. Copy and paste that hostname into your
browser to test out the Booker application.

We can simplify all these steps by creating an OpenShift template that
allows us to create all these resources with a single command.

All the services will start and cluster with each other, but the
actual functionality of searching and purchasing books doesn't work
yet. Booker assumes every microservice advertises a service URL that
the browser can access and currently we're only advertising internal
IPs for each service. The fix for this is to translate those internal
IPs to routable hostnames and to expose each service (via `oc
expose`). We already do this when locating the Keycloak service, but
it's a bit more complicated to do this for the other Booker services.
