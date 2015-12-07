package org.wildfly.swarm.booker;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.undertow.WARArchive;
import org.wildfly.swarm.booker.common.ContainerUtils;

/**
 * @author Lance Ball
 */
public class Main {
    public static void main(String... args) throws Exception {
        Container container = new Container();
        container.fraction(ContainerUtils.loggingFraction());
        container.start();
        WARArchive war = ShrinkWrap.create(WARArchive.class);
        war.staticContent();
        war.addAllDependencies();
        container.deploy(war);
    }
}
