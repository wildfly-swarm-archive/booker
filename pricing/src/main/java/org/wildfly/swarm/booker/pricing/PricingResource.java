package org.wildfly.swarm.booker.pricing;

import org.keycloak.KeycloakPrincipal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class PricingResource {

    @GET
    @Path("/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Integer search(@PathParam("id") String id, @Context SecurityContext context) {
        KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();
        if ( principal != null && principal.getKeycloakSecurityContext() != null ) {
            return 9;
        }
        return 10;
    }
}
