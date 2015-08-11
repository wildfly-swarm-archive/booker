package org.wildfly.swarm.booker.pricing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class PricingResource {

    @GET
    @Path("/book")
    @Produces("application/json")
    public Integer search(@QueryParam("id") String id, @Context SecurityContext context) {
        KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();
        if ( principal != null && principal.getKeycloakSecurityContext() != null ) {
            return 9;
        }
        return 10;
    }
}
