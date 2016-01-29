
# Running the Booker demo using docker and plain maven

The following contains brief instructions how to setup the booker demo using  docker images for some of the system components.

## Prerequisites

- Docker Toolbox: You'll need the docker toolbox to run containers on your machine: https://www.docker.com/products/docker-toolbox
- Java 8
- Maven 3.3.3 (prior versions had issues with the wildfly-swarm plugin)


# Prepare the docker images

In the following we assume docker is running on `192.168.99.100`. See the 'FAQ' on how to determine the address on your machine.

## Consul (http://consul.io)

With Swarm you have two choices for providing service registration and discovery: jgroups or consul.
In this setup we use consul, because it's much more straightforward to configure and use:
The main interaction with the registry happens through HTTP.

The progrium consul lends itself well to launch consul. Simply pull the image:

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

Pull the image, but beware of the specific version (`1.7.0.Final`) we use here:

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
cd BOOKER_HOME
mvn clean install
```

After this you can head on and start each service in it's own JVM. Services run with a port-poffset (`-Dswarm.port.offset=...`) and require a reference to the Keycloak authentication service (`export BOOKER_KEYCLOAK_SERVICE_HOST=...`)

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

If you move back to consul web ui (`http://192.168.99.100:8500`) you should see
three backend services registered with consul, exposing to their IP addresses and being in a 'healthy' state.

You can also query the registry from the command line:
(See also https://www.consul.io/docs/agent/http.html)

```
curl 192.168.99.100:8500/v1/health/node/node1 | pretty.json
[   
    {
        "CheckID": "service:library:127.0.0.1:8084",
        "Name": "Service 'library' check",
        "Node": "node1",
        "Notes": "",
        "Output": "",
        "ServiceID": "library:127.0.0.1:8084",
        "ServiceName": "library",
        "Status": "passing"
    },
    {
        "CheckID": "service:pricing:127.0.0.1:8230",
        "Name": "Service 'pricing' check",
        "Node": "node1",
        "Notes": "",
        "Output": "",
        "ServiceID": "pricing:127.0.0.1:8230",
        "ServiceName": "pricing",
        "Status": "passing"
    },
    {
        "CheckID": "service:store:127.0.0.1:8130",
        "Name": "Service 'store' check",
        "Node": "node1",
        "Notes": "",
        "Output": "",
        "ServiceID": "store:127.0.0.1:8130",
        "ServiceName": "store",
        "Status": "passing"
    }
]
```


## Launch the demo store

Once the above steps have been performed successfully, you are ready to launch the actual web interface.
It leverages the three backend services (library, pricing and store), the service registry and the authentication service.

```
http://localhost:8280
```

> NOTE: Some browsers don't successfully work in this scenario. We've tested with recent chrome versions and seems to work fine. But firefox seems to have problems.

# FAQ

### Wheres my docker machine running?
The default docker toolbox uses `192.168.99.100`, but in some cases the IP may be different. To find out the base IP of the docker machine simply use:

```
docker-machine ip default
192.168.99.100
```

### Why does web interface not work?

Some browser seem to have problems with the current web interface implementation. There is a lot of trickery with same origin restrictions in this setup and some javascript wizardry going on. If you run into problem check the browser javascript log and network communication traces. If in doubt, ping us on IRC.

### What's the default Keycloak login?

On installation time it's set to `admin:admin`. After your first successful login you are asked to change it.

### I get strange SOP errors (Same Origin Policy)

Did you `export BOOKER_KEYCLOAK_SERVICE_HOST=192.168.99.100`? Or maybe try a different browser? 
