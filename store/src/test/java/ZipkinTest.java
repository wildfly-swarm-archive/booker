import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.wildfly.swarm.jaxrs.btm.zipkin.ClientRequestInterceptor;
import org.wildfly.swarm.jaxrs.btm.zipkin.ClientResponseInterceptor;

/**
 * @author Heiko Braun
 * @since 07/10/16
 */
public class ZipkinTest {

    @Test
    @Ignore
    public void testSpanLogging() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(ClientRequestInterceptor.class);
        client.register(ClientResponseInterceptor.class);
        WebTarget target = client.target("http://localhost:8082").path("search");

        javax.ws.rs.core.Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));

    }

    public static void main(String[] args) {
        ZipkinTest zipkinTest = new ZipkinTest();

        for(int i=0; i<100; i++) {

            zipkinTest.testSpanLogging();

        }
    }
}
