package net.serenitybdd.integration.jenkins.logging;

import org.slf4j.Logger;

public class InfoWitness implements Witness {
    private final Logger logger;

    public InfoWitness(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void note(String msg) {
        logger.info(msg);
    }

    @Override
    public void note(String format, Object arg) {
        logger.info(format, arg);
    }

    @Override
    public void note(String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
    }

    @Override
    public void note(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void note(String msg, Throwable t) {
        logger.info(msg, t);
    }
}
