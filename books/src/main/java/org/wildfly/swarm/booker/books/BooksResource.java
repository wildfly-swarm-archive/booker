package org.wildfly.swarm.booker.books;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class BooksResource {

    @GET
    @Produces("application/json")
    public Object get() {
        Map<String,Object> books = new HashMap<>();

        return books;
    }
}
