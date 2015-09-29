package org.wildfly.swarm.booker.store;

import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import io.netty.buffer.ByteBuf;
import rx.Observable;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class StoreResource {

    @Inject
    private Store store;

    @Inject
    private PricingService pricingService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/search")
    @Produces("application/json")
    public Store.SearchResult search(@QueryParam("q") String q, @QueryParam("page") Integer page) {
        if (q == null) {
            return Store.SearchResult.EMPTY;
        }
        if (page == null) {
            page = 1;
        }
        Store.SearchResult results = this.store.search(q, page - 1);
        results.setRequestUri(uriInfo.getBaseUri().toString());
        return results;
    }

    @GET
    @Path("/book")
    @Produces("application/json")
    public void get(@Suspended final AsyncResponse asyncResponse, @QueryParam("id") String id) {
        Book book = this.store.get(id);
        Observable<ByteBuf> obs = pricingService.get(id).observe();
        obs.subscribe(
                (result) -> {
                    int price = Integer.parseInt(result.toString(Charset.defaultCharset()));
                    book.setPrice(price);
                    asyncResponse.resume(book);
                },
                (err) -> {
                    asyncResponse.resume(err);
                }
        );
    }
}
