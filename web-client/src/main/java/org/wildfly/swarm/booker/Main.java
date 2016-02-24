package org.wildfly.swarm.booker;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.booker.common.ContainerUtils;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.topology.webapp.TopologyProperties;
import org.wildfly.swarm.topology.webapp.TopologyWebAppFraction;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * @author Lance Ball
 */
public class Main {
    public static void main(String... args) throws Exception {
        Container container = new Container();
        container.fraction(ContainerUtils.loggingFraction());

        System.setProperty(TopologyProperties.CONTEXT_PATH, "/topology-webapp");
        TopologyWebAppFraction topologyWebAppFraction = new TopologyWebAppFraction();
        topologyWebAppFraction.proxyService("store", "/store-proxy");
        topologyWebAppFraction.proxyService("pricing", "/pricing-proxy");
        topologyWebAppFraction.proxyService("library", "/library-proxy");
        container.fraction(topologyWebAppFraction);
        container.start();

        WARArchive war = ShrinkWrap.create(WARArchive.class);
        war.staticContent();
        war.addAllDependencies();
        war.addModule("org.wildfly.swarm.container");
        container.deploy(war);
    }
}
