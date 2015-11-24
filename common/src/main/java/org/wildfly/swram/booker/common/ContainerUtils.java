package org.wildfly.swram.booker.common;

import org.wildfly.swarm.config.logging.Logger;
import org.wildfly.swarm.jgroups.JGroupsFraction;
import org.wildfly.swarm.logging.LoggingFraction;

public class ContainerUtils {

    public static LoggingFraction loggingFraction() {
        LoggingFraction fraction = new LoggingFraction()
                .defaultColorFormatter()
                .consoleHandler(LoggingFraction.Level.TRACE, LoggingFraction.COLOR_PATTERN)
                .rootLogger(LoggingFraction.Level.INFO, LoggingFraction.CONSOLE);

        fraction.logger(new Logger("org.openshift.ping")
                .level(LoggingFraction.Level.TRACE));
        fraction.logger(new Logger("org.jgroups.protocols.openshift")
                .level(LoggingFraction.Level.TRACE));

        return fraction;
    }
}
