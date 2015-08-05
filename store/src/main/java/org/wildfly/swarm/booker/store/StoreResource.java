package org.wildfly.swarm.booker.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class StoreResource {


    @Inject
    private Store store;

    @GET
    @Path("/search")
    @Produces("application/json")
    public Store.SearchResult search(@QueryParam("q") String q, @QueryParam("page") Integer page) {
        System.err.println( "Search: " + q );
        if ( q == null ) {
            return Store.SearchResult.EMPTY;
        }
        if (page == null ) {
            page = 1;
        }
        return this.store.search(q, page - 1);
    }
}
