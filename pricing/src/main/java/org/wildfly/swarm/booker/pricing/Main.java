package org.wildfly.swarm.booker.pricing;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.keycloak.Secured;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;
import org.wildfly.swarm.booker.common.ContainerUtils;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String... args) throws Exception {

        Container container = new Container();
        container.fraction(ContainerUtils.loggingFraction());

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());
        deployment.as(RibbonArchive.class).advertise("pricing");
        deployment.as(Secured.class);
        ContainerUtils.addExternalKeycloakJson(deployment);

        container.start();
        container.deploy(deployment);

    }
}
