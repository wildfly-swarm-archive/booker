package org.wildfly.swarm.booker;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.Environment;
import org.wildfly.swarm.ribbon.webapp.RibbonWebAppFraction;
import org.wildfly.swarm.undertow.WARArchive;
import org.wildfly.swarm.booker.common.ContainerUtils;

/**
 * @author Lance Ball
 */
public class Main {
    public static void main(String... args) throws Exception {
        Container container = new Container();
        container.fraction(ContainerUtils.loggingFraction());

        RibbonWebAppFraction ribbonWebAppFraction = new RibbonWebAppFraction();
        if (Environment.openshift()) {
            ribbonWebAppFraction.externalAddressMapper(OpenshiftExternalAddressMapper.class);
        }
        container.fraction(ribbonWebAppFraction);
        container.start();

        WARArchive war = ShrinkWrap.create(WARArchive.class);
        war.staticContent();
        war.addAllDependencies();
        war.addModule("org.wildfly.swarm.container");
        war.addClass(OpenshiftExternalAddressMapper.class);
        container.deploy(war);
    }
}
