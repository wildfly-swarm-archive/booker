package org.wildfly.swarm.booker;

import org.wildfly.swarm.booker.common.Discoverer;
import org.wildfly.swarm.topology.ExternalAddressMapper;
import org.wildfly.swarm.topology.Topology;

import java.io.IOException;

public class OpenshiftExternalAddressMapper implements ExternalAddressMapper {
    @Override
    public Topology.Entry toExternal(Topology.Entry internalServer, int defaultPort) {
        try {
            String externalHost = Discoverer.serviceHostToExternalHost(internalServer.getAddress());
            return new Topology.Entry() {
                @Override
                public String getAddress() {
                    return externalHost;
                }

                @Override
                public int getPort() {
                    return defaultPort;
                }
            };
        } catch (IOException ex) {
            ex.printStackTrace();
            return internalServer;
        }
    }
}
