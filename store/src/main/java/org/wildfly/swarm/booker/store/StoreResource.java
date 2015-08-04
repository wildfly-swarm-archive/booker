package org.wildfly.swarm.booker.store;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class StoreResource {

    @Path("/search")
    @Produces("application/json")
    public Object search() {
        Map<String,Object> searchResults = new HashMap<>();

        return searchResults;
    }
}
