package org.wildfly.swarm.booker.common;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.wildfly.swarm.config.logging.Logger;
import org.wildfly.swarm.jgroups.JGroupsFraction;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.config.logging.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ContainerUtils {

    public static LoggingFraction loggingFraction() {
        LoggingFraction fraction = LoggingFraction.createDefaultLoggingFraction();

        fraction.logger(new Logger("org.openshift.ping")
                .level(Level.TRACE));
        fraction.logger(new Logger("org.jgroups.protocols.openshift")
                .level(Level.TRACE));

        return fraction;
    }

    public static void addExternalKeycloakJson(Archive archive) {
        String keycloakPath = "WEB-INF/keycloak.json";
        Node keycloakJson = archive.get(keycloakPath);
        if (keycloakJson == null) {
            return;
        }
        InputStream inputStream = keycloakJson.getAsset().openStream();
        StringBuilder str = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String externalKeycloakUrl = Discoverer.externalKeycloakUrl(80);
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("http://localhost:9090", externalKeycloakUrl);
                str.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        archive.add(new ByteArrayAsset(str.toString().getBytes()), keycloakPath);
    }
}
