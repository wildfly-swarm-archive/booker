package org.wildfly.swarm.booker.library;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.booker.common.ContainerUtils;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.keycloak.Secured;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String... args) throws Exception {

        Swarm container = new Swarm();
        container.fraction(ContainerUtils.loggingFraction());
        container.fraction(new JPAFraction()
                                   .defaultDatasource("LibraryDS"));

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());
        deployment.as(RibbonArchive.class).advertise("library");
        deployment.as(Secured.class)
                .protect("/items")
                .withMethod("GET")
                .withRole("*");
        ContainerUtils.addExternalKeycloakJson(deployment);


        deployment.add(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "WEB-INF/classes/META-INF/persistence.xml");
        deployment.add(new ClassLoaderAsset("project-stages.yml", Main.class.getClassLoader()), "WEB-INF/classes/project-stages.yml");
        deployment.addAllDependencies();
        container.start();
        container.deploy(deployment);

    }
}
