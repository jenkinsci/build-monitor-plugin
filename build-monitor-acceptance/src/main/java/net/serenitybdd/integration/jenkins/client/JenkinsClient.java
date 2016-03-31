package net.serenitybdd.integration.jenkins.client;

import com.google.common.base.Joiner;
import net.serenitybdd.integration.jenkins.logging.ErrorWitness;
import net.serenitybdd.integration.jenkins.logging.InfoWitness;
import net.serenitybdd.integration.jenkins.logging.LoggerOutputStream;
import net.serenitybdd.integration.jenkins.process.JenkinsProcess;
import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;

import static net.serenitybdd.integration.jenkins.process.JenkinsProcess.JENKINS_IS_FULLY_UP_AND_RUNNING;

public class JenkinsClient {
    private final static Logger logger = LoggerFactory.getLogger(JenkinsClient.class);

    private final JenkinsProcess process;
    private final JenkinsClientExecutor executor;

    public JenkinsClient(JenkinsClientExecutor executor, JenkinsProcess process) {
        this.executor = executor;
        this.process = process;
    }


    // user accounts
    // https://gist.github.com/hayderimran7/50cb1244cc1e856873a4
    // http://stackoverflow.com/questions/17716242/creating-user-in-jenkins-via-api

    // todo: remove as it's no longer needed; a similar mechanism will be needed to setup user accounts though
    public void populateUpdateCenterCaches() {
        logger.info("FETCHING UPDATE CENTER");

        Promise<Matcher, ?, ?> promise = process.promiseWhen("Obtained the latest update center data file for UpdateSource default");

        executeGroovy(
            "def ucUrl = new URL('http://updates.jenkins-ci.org/update-center.json')",
            "def json  = hudson.model.DownloadService.loadJSON(ucUrl)",
            "def site  = jenkins.model.Jenkins.instance.updateCenter.getById('default')",
            "site.updateData(json, false)"
        );

        try {
            promise.waitSafely(10 * 1000);
            logger.info("UPDATE CENTER RELOADED");
        } catch (InterruptedException e) {
            throw new RuntimeException("Couldn't update the Update Center caches.", e);
        }
    }

    public void installPlugin(String pluginName) {
        // http://stackoverflow.com/questions/7709993/how-can-i-update-jenkins-plugins-from-the-terminal

        executeCommand("install-plugin", pluginName, "-restart");
        process.waitUntil(JENKINS_IS_FULLY_UP_AND_RUNNING);
    }

    public void installPlugins(List<String> plugins) {
        for (String pluginName : plugins) {
            executeCommand("install-plugin", pluginName);
        }

        safeRestart();
    }


    public void safeRestart() {
        executeCommand("safe-restart");

        process.waitUntil(JENKINS_IS_FULLY_UP_AND_RUNNING);
    }

    private int executeGroovy(String... groovyScriptLines) {
        String script = Joiner.on(";\n").join(groovyScriptLines);

        return executor.call("groovy", "=").execute(withInput(script), info(logger), error(logger));
    }


    private int executeCommand(String... args) {
        return executor.call(args).execute(noManualInput(), info(logger), error(logger));
    }

    private InputStream noManualInput() {
        return withInput("");
    }

    private InputStream withInput(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    private OutputStream info(Logger logger) {
        return new LoggerOutputStream(new InfoWitness(logger));
    }

    private OutputStream error(Logger logger) {
        return new LoggerOutputStream(new ErrorWitness(logger));
    }
}
