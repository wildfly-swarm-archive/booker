package org.wildfly.swarm.booker.store;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.stream.XMLStreamException;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
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

        if (System.getProperty("swarm.gutenberg.data") != null) {
            //int limit = Integer.MAX_VALUE;
            int limit = 5000;
            AtomicInteger counter = new AtomicInteger(0);
            RDFProcessor processor = new RDFProcessor(Paths.get("src", "main", "resources", "META-INF", "store.xml"));
            Files.walkFileTree(Paths.get(System.getProperty("swarm.gutenberg.data")), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        processor.process(file);
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }
                    if (counter.incrementAndGet() > limit) {
                        return FileVisitResult.TERMINATE;
                    }
                    if (counter.get() % 1000 == 0) {
                        System.err.print('.');
                    }
                    return super.visitFile(file, attrs);
                }
            });

            processor.close();
            System.exit(0);
        }



        Swarm container = new Swarm();
        container.fraction(ContainerUtils.loggingFraction());
        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addAsLibrary(container.createDefaultDeployment());
        deployment.as(RibbonArchive.class).advertise("store");
        deployment.as(Secured.class);
        ContainerUtils.addExternalKeycloakJson(deployment);
        deployment.addAllDependencies();


        container.deploy(deployment);

    }
}
