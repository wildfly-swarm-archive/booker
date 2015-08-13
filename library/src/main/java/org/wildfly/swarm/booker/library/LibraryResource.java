package org.wildfly.swarm.booker.library;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;

/**
 * @author Bob McWhirter
 */
@Path("/")
@Stateless
public class LibraryResource {

    @Inject
    EntityManager em;

    @GET
    @Produces("application/json")
    @Path("/items")
    public List<LibraryItem> get(@Context SecurityContext context) {
        KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();
        String userId = principal.getName();
        List<LibraryItem> list = new ArrayList<>();
        TypedQuery<LibraryItem> q = this.em.createQuery("SELECT li FROM LibraryItem li WHERE li.userId = :userId", LibraryItem.class);
        return q.setParameter("userId", userId).getResultList();
    }

    @POST
    @Produces("application/json")
    @Path("/items")
    public LibraryItem addItem(@Context SecurityContext context, @FormParam("id") String bookId) throws URISyntaxException {
        KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();
        String userId = principal.getName();
        LibraryItem item = new LibraryItem(userId, bookId);
        em.persist(item);
        return item;
    }
}
