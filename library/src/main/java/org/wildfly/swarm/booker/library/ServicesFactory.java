package org.wildfly.swarm.booker.library;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.wildfly.swarm.netflix.ribbon.secured.client.SecuredRibbon;

/**
 * @author Bob McWhirter
 */
@ApplicationScoped
public class ServicesFactory {

    @Produces
    @ApplicationScoped
    public static StoreService getInstance() {
        return SecuredRibbon.from(StoreService.class);
    }
}
