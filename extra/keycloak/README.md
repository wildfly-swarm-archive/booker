# Download Keycloak

    http://downloads.jboss.org/keycloak/2.1.0.Final/keycloak-2.1.0.Final.zip

# Launch

Launch Keycloak on port 9090 and import `booker.json`

    ./bin/standalone.sh -Djboss.http.port=9090

# Import realm details

Using the web UI, import `booker.json`

The realm will be created, and a user named 'bob' with the password
of 'bob' will be avilable.
