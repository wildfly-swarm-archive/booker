package org.wildfly.swarm.booker.store;

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
    public static PricingService getInstance() {
        return SecuredRibbon.from(PricingService.class);
    }
}
