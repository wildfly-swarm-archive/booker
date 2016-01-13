package org.wildfly.swarm.booker;

import org.wildfly.swarm.booker.common.Discoverer;
import org.wildfly.swarm.netflix.ribbon.RibbonExternalAddressMapper;
import org.wildfly.swarm.netflix.ribbon.RibbonServer;

import java.io.IOException;

public class OpenshiftExternalAddressMapper implements RibbonExternalAddressMapper {
    @Override
    public RibbonServer toExternal(RibbonServer internalServer, int defaultPort) {
        try {
            String externalHost = Discoverer.serviceHostToExternalHost(internalServer.getHost());
            return new RibbonServer(externalHost, defaultPort);
        } catch (IOException ex) {
            ex.printStackTrace();
            return internalServer;
        }
    }
}
