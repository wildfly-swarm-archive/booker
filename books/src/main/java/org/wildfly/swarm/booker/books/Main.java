package org.wildfly.swarm.booker.books;

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
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String... args) throws Exception {


        Container container = new Container();
        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());
        deployment.as(RibbonArchive.class).setApplicationName("books");
        container.start();
        container.deploy(deployment);

    }
}
