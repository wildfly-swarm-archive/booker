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
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.booker.common.ContainerUtils;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jaxrs.btm.ZipkinFraction;
import org.wildfly.swarm.keycloak.Secured;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;

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
        /*
        enable this for remote zipkin reporting

        container.fraction(
                new ZipkinFraction("booker-store")
                        .reportAsync("http://localhost:9411/api/v1/spans")
                        .sampleRate(0.1f) // keep 10%
        );*/

        container.fraction(new ZipkinFraction("booker-store"));
        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());
        deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");
        deployment.add(new ClassLoaderAsset("META-INF/store.xml", Main.class.getClassLoader()), "WEB-INF/classes/store.xml");
        deployment.as(RibbonArchive.class).advertise("store");
        deployment.as(Secured.class);
        ContainerUtils.addExternalKeycloakJson(deployment);
        deployment.addAllDependencies();


        container.deploy(deployment);

    }
}
