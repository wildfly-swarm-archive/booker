package org.wildfly.swarm.booker.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class LibraryResource {

    @Inject
    EntityManager em;

    @GET
    @Produces("application/json")
    @Path( "/items")
    public List<LibraryItem> getByUserId(@Context SecurityContext context) {
        System.err.println( "context: " + context.getUserPrincipal() );
        System.err.println( "em: " + this.em );
        KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();

        String userId = principal.getName();

        List<LibraryItem> list = new ArrayList<>();
        TypedQuery<LibraryItem> q = this.em.createQuery("SELECT * FROM LIBRARY_ITEMS li WHERE li.userId = :userId", LibraryItem.class);
        return q.setParameter( "userId", userId ).getResultList();
    }
}
