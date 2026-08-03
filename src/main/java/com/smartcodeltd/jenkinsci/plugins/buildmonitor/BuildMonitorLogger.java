package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildMonitorLogger {
    private final String className;

    public static <T> BuildMonitorLogger forClass(Class<T> sourceClass) {
        return new BuildMonitorLogger(sourceClass.getName());
    }

    public void debug(String sourceMethod, String template, Object... arguments) {
        log(Level.FINEST, sourceMethod, template, arguments);
    }

    public void info(String sourceMethod, String template, Object... arguments) {
        log(Level.INFO, sourceMethod, template, arguments);
    }

    public void warning(String sourceMethod, String template, Object... arguments) {
        log(Level.WARNING, sourceMethod, template, arguments);
    }

    public void error(String sourceMethod, String template, Object... arguments) {
        log(Level.SEVERE, sourceMethod, template, arguments);
    }

    // --

    private final Logger logger;

    private BuildMonitorLogger(String className) {
        this.className = className;
        this.logger = Logger.getLogger("Build Monitor");
    }

    private void log(Level level, String sourceMethod, String template, Object... arguments) {
        logger.logp(level, className, sourceMethod, template, arguments);
    }
}
