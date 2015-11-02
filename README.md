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

## `Vagrant`
A Vagrantfile and support scripts to install and run booker in a
virtual machine. Requires Virtualbox and Vagrant be installed.
