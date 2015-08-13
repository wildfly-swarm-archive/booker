package org.wildfly.swarm.booker.library;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.ResourceGroup;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import com.netflix.ribbon.proxy.annotation.Var;
import io.netty.buffer.ByteBuf;

/**
 * @author Bob McWhirter
 */
@ResourceGroup(name = "store")
public interface StoreService {

    @TemplateName("get")
    @Http(
            method = Http.HttpMethod.GET,
            uri = "/book?id={bookId}"
    )
    RibbonRequest<ByteBuf> get(@Var("bookId")String bookId);

}

