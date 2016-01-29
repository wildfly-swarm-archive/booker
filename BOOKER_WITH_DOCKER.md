
# Running the Booker demo using docker and plain maven

The following contains brief instructions how to setup the booker demo using  docker images for some system components.

## Prerequisites

- Docker Toolbox: You'll need the docker toolbox to run containers on your machine: https://www.docker.com/products/docker-toolbox
- Java 8
- Maven 3.3.3 (prior version has some issues with the wildfly-swarm plugin)


# Prepare the docker images

In the following we assume docker is running on `192.168.99.100`. See the section 'Caveats' to find how to identify this address on your machine.

## Consul (http://consul.io)

You have two choices for providing service registration and discovery: jgroups or consul. In this setup we use consul, because much more straightforward to configure and diagnose, because everything is HTTP based.

We leverage the progrium consul image to launch consul. Simply pull the image:

```
docker pull progrium/consul
```

And start it:

```
docker run -d -p 8400:8400 -p 8500:8500 -p 8600:53/udp -h node1 progrium/consul -server -bootstrap -ui-dir /ui
```

You can verify if it runs successfully by navigating to the consul web UI:

```
http://192.168.99.100:8500
```

## Keycloak (http://keycloak.jboss.org/)

Keycloak is used to secure access to the service endpoints. 

Pull the image, but beware of the specific version we use here:

```
docker pull jboss/keycloak:1.7.0.Final
```

And start it:

```
docker run -d -p 9090:8080 jboss/keycloak:1.7.0.Final
```

Once you started keycloak, navigate to the admin interface and import the booker security realm. It can be found at `extra/keycloak/booker.json`:

```
http://192.168.99.100:9090/auth
```

# Prepare the Wildfly Swarm services

Make you've successful build the top level project before you move on:

```
cd $BOOKER
mvn clean install
```

 After this you can head on and start each and every service in it's own JVM. Each service get's it's own port offset (`-Dswarm.port.offset=...`) and a reference to the keycloak authentication service (`export BOOKER_KEYCLOAK_SERVICE_HOST=...`)

## Build and start each service
 
### Library service

```
cd library
export BOOKER_KEYCLOAK_SERVICE_HOST=192.168.99.100
mvn wildfly-swarm:run -Dswarm.consul.url=http://192.168.99.100:8500/
```

### Pricing Service

```
cd library
export BOOKER_KEYCLOAK_SERVICE_HOST=192.168.99.100
mvn wildfly-swarm:run -Dswarm.consul.url=http://192.168.99.100:8500/ -Dswarm.port.offset=150
```

### Store Service
```
cd store
export BOOKER_KEYCLOAK_SERVICE_HOST=192.168.99.100
mvn clean wildfly-swarm:run -Dswarm.consul.url=http://192.168.99.100:8500/ -Dswarm.port.offset=50
```

### Booker Web Interface
```
cd web-client
export BOOKER_KEYCLOAK_SERVICE_HOST=192.168.99.100
mvn clean wildfly-swarm:run -Dswarm.consul.url=http://192.168.99.100:8500/ -Dswarm.port.offset=200
```

## Verify all services have been registered

If you move back to consul web ui (`http://192.168.99.100:8500`) you should see that the three backend services (library, pricing and store) are registered with consul, pointing to their corresponding IP addresses and being in a 'healthy' state.

## Launch the demo store

Once the above steps have been performed successfully, you are ready to launch the actual web interface, that leverages the three backend services, the service registry and the authentication service.

```
http://localhost:8280
```

> NOTE: Some browsers don't successfully work in this scenario. We've tested with recent chrome versions and seems to work fine. Firefox seems to have problems however.

# FAQ

## Wheres my docker machine running?

```
docker-machine ip default
192.168.99.100
```

## Why does web interface not work?

See above. Some browser seem to have problems with the current web interface implementation. 

## What's the default keycloak login?

On installation time it's set to `admin:admin`. After your first successful login you are asked to change it. 

## I get strange SOP errors (Same Origin Policy)

Did you `export BOOKER_KEYCLOAK_SERVICE_HOST=192.168.99.100`? maybe try a different browser?