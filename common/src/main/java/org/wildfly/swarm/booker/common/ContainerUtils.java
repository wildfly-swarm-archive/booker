package org.wildfly.swarm.booker.common;

import org.wildfly.swarm.config.logging.Logger;
import org.wildfly.swarm.jgroups.JGroupsFraction;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.config.logging.Level;

public class ContainerUtils {

    public static LoggingFraction loggingFraction() {
        LoggingFraction fraction = LoggingFraction.createDefaultLoggingFraction();

        fraction.logger(new Logger("org.openshift.ping")
                .level(Level.TRACE));
        fraction.logger(new Logger("org.jgroups.protocols.openshift")
                .level(Level.TRACE));

        return fraction;
    }
}
