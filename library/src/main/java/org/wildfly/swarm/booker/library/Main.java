package org.wildfly.swarm.booker.library;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.Datasource;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.datasources.Driver;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.keycloak.Secured;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String... args) throws Exception {


        Container container = new Container();
        container.fraction(new JPAFraction()
                .inhibitDefaultDatasource()
                .defaultDatasourceName("LibraryDS"));
        container.fraction(new DatasourcesFraction()
                .driver(new Driver("h2")
                        .datasourceClassName("org.h2.Driver")
                        .xaDatasourceClassName("org.h2.jdbcx.JdbcDataSource")
                        .module("com.h2database.h2"))
                .datasource(new Datasource("LibraryDS")
                        .driver("h2")
                        .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                        .authentication("sa", "sa")));

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());
        deployment.as(RibbonArchive.class).setApplicationName("library");
        deployment.as(Secured.class);//.protect("/items").withRole("*");
        deployment.add(new ClassLoaderAsset("META-INF/persistence.xml"), "WEB-INF/classes/META-INF/persistence.xml");
        container.start();
        container.deploy(deployment);

    }
}
