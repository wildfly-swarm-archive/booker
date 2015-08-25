package org.wildfly.swarm.booker.store;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.netflix.ribbon.proxy.annotation.ResourceGroup;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import com.netflix.ribbon.proxy.annotation.Var;
import io.netty.buffer.ByteBuf;

/**
 * @author Bob McWhirter
 */
@ResourceGroup(name="pricing")
public interface PricingService {

    @TemplateName("get")
    @Http(
            method = Http.HttpMethod.GET,
            uri = "/book/{id}"
    )
    RibbonRequest<ByteBuf> get(@Var("id") String id);
}
