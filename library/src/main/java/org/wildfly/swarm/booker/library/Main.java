package org.wildfly.swarm.booker.library;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.config.datasources.JDBCDriver;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;
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
        container.fraction(new JPAFraction()
                .inhibitDefaultDatasource()
                .defaultDatasource("LibraryDS"));

        container.fraction(new DatasourcesFraction()
                .jdbcDriver(new JDBCDriver("h2")
                        .driverName("h2")
                        .driverDatasourceClassName("org.h2.Driver")
                        .xaDatasourceClass("org.h2.jdbcx.JdbcDataSource")
                        .driverModuleName("com.h2database.h2"))
                .dataSource(new DataSource("LibraryDS")
                        .driverName("h2")
                        .jndiName("java:/LibraryDS")
                        .connectionUrl("jdbc:h2:./library;DB_CLOSE_ON_EXIT=TRUE")
                        .userName("sa")
                        .password( "sa" )));

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());
        deployment.as(RibbonArchive.class).setApplicationName("library");
        deployment.as(Secured.class)
                .protect("/items")
                .withMethod("GET")
                .withRole("*");
        ContainerUtils.addExternalKeycloakJson(deployment);


        deployment.add(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "WEB-INF/classes/META-INF/persistence.xml");
        deployment.addAllDependencies();
        container.start();
        container.deploy(deployment);

    }
}
