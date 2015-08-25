package org.wildfly.swarm.booker.library;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import org.keycloak.KeycloakPrincipal;
import rx.Observable;

/**
 * @author Bob McWhirter
 */
@Path("/")
@Stateless
public class LibraryResource {

    @Inject
    StoreService store;

    @Inject
    EntityManager em;

    @GET
    @Produces("application/json")
    @Path("/items")
    public void get(@Suspended final AsyncResponse asyncResponse, @Context SecurityContext context) {
        KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();
        String userId = principal.getName();
        TypedQuery<LibraryItem> q = this.em.createQuery("SELECT li FROM LibraryItem li WHERE li.userId = :userId", LibraryItem.class);
        List<LibraryItem> items = q.setParameter("userId", userId).getResultList();

        Observable<List<LibraryItem>> root = Observable.just(new ArrayList<>());
        for (LibraryItem each : items) {
            Observable<ByteBuf> obs = store.get(each.getBookId()).observe();
            root = root.zipWith(obs, ((libraryItems, byteBuf) -> {
                ObjectMapper mapper = new ObjectMapper();
                ObjectReader reader = mapper.reader();
                JsonFactory factory = new JsonFactory();
                try {
                    JsonParser parser = factory.createParser(new ByteBufInputStream(byteBuf));
                    Map map = reader.readValue(parser, Map.class);
                    each.setTitle((String) map.get("title"));
                    each.setAuthor((String) map.get("author"));
                } catch (IOException e) {
                }
                libraryItems.add(each);
                return libraryItems;
            }));
        }

        root.subscribe(
                (result) -> asyncResponse.resume(result),
                (err) -> asyncResponse.resume(err)
        );
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
