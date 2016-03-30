package net.serenitybdd.integration.jenkins.logging;

import org.slf4j.Logger;

public class ErrorWitness implements Witness {
    private final Logger logger;

    public ErrorWitness(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void note(String msg) {
        logger.error(msg);
    }

    @Override
    public void note(String format, Object arg) {
        logger.error(format, arg);
    }

    @Override
    public void note(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    @Override
    public void note(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void note(String msg, Throwable t) {
        logger.error(msg, t);
    }
}
