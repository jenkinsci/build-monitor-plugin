package net.serenitybdd.integration.jenkins.client;

import hudson.cli.CLI;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import net.serenitybdd.integration.jenkins.process.JenkinsProcess;
import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsClient {
    private static final Logger logger = LoggerFactory.getLogger(JenkinsClient.class);
    private static final int Max_Wait_Time = 5 * 60 * 1000;
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String OS_VERSION = System.getProperty("os.version").toLowerCase();

    private final JenkinsProcess process;
    private final URL jenkinsUrl;

    public JenkinsClient(URL jenkinsUrl, JenkinsProcess process) {
        this.process = process;
        this.jenkinsUrl = jenkinsUrl;
    }

    // user accounts
    // https://gist.github.com/hayderimran7/50cb1244cc1e856873a4
    // http://stackoverflow.com/questions/17716242/creating-user-in-jenkins-via-api

    public void registerAccount(String username, String password) {
        logger.info("Enabling Jenkins Security and registering account for '{}', identified by '{}'", username, password);

        String endScriptMessage = String.format("Account for '%s' created", username);
        Promise<Matcher, ?, ?> promise = process.promiseWhen(endScriptMessage);

        try {
            executeGroovy(promise,
                    "def instance         = jenkins.model.Jenkins.get()",
                    "def usersCanRegister = true",
                    "def realm            = new hudson.security.HudsonPrivateSecurityRealm(usersCanRegister)",
                    String.format("realm.createAccount(\"%s\",\"%s\")", username, password),
                    "instance.setSecurityRealm(realm)",
                    "instance.save()",
                    "",
                    "import java.util.logging.Logger",
                    "Logger rootLogger = Logger.getLogger('')",
                    String.format("rootLogger.info(\"%s\")", endScriptMessage)
            );
            logger.info(endScriptMessage);
        } catch (InterruptedException e) {
            throw new RuntimeException("Couldn't enable Jenkins Security", e);
        }
    }

    public void populateUpdateCenterCaches() {
        logger.info("FETCHING UPDATE CENTER");

        String endScriptMessage = "UPDATE CENTER RELOADED";
        Promise<Matcher, ?, ?> promise = process.promiseWhen(endScriptMessage);

        try {
            executeGroovy(promise,
                    "def ucUrl = new URL('http://updates.jenkins-ci.org/update-center.json')",
                    "def json  = hudson.model.DownloadService.loadJSON(ucUrl)",
                    "def site  = jenkins.model.Jenkins.instance.updateCenter.getById('default')",
                    "site.updateData(json, false)",
                    "",
                    "import java.util.logging.Logger",
                    "Logger rootLogger = Logger.getLogger('')",
                    String.format("rootLogger.info(\"%s\")", endScriptMessage)
                );
            logger.info(endScriptMessage);
        } catch (InterruptedException e) {
            throw new RuntimeException("Couldn't update the Update Center caches.", e);
        }
    }

    public void installPlugin(String pluginName) {
        // http://stackoverflow.com/questions/7709993/how-can-i-update-jenkins-plugins-from-the-terminal

        executeCommand("install-plugin", pluginName, "-restart");
        process.waitUntil(JenkinsProcess.JENKINS_IS_FULLY_UP_AND_RUNNING);
    }


    public void installPlugins(List<String> plugins) {
        for (String pluginName : plugins) {
            executeCommand("install-plugin", pluginName);
        }

        restart();
    }

    private void restart() {
        //Both Windows and WSL needs hard restart
        if (OS.contains("win") || OS_VERSION.contains("microsoft")) {
            hardRestart();
        } else {
            safeRestart();
        }
    }

    private void hardRestart() {
        try {
            safeShutdown();
            process.start();
            //Note: Do NOT wait here for Jenkins startup as that is handled within process.start()
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        process.getJenkinsLogWatcher().close();
        executeCommand("shutdown");
        process.stop();
    }

    public void safeShutdown() {
        process.getJenkinsLogWatcher().close();
        executeCommand("safe-shutdown");
        process.stop();
    }

    private void safeRestart() {
        executeCommand("safe-restart");

        process.waitUntil(JenkinsProcess.JENKINS_IS_FULLY_UP_AND_RUNNING);
    }

    public void setExternalBuildResult(String projectName, String result) {
        executeCommand("set-external-build-result",
                "--job", projectName,
                "--result", result,
                "--log", String.format("%s finished with %s", projectName, result)
        );
    }

    private synchronized int executeGroovy(Promise<Matcher, ?, ?> promise, String... groovyScriptLines) throws InterruptedException {
        String script = String.join(";\n", groovyScriptLines);

        //TODO use RealJenkinsRule
        //return executor.call("groovy", "=").execute(withInput(script), info(logger), error(logger));
        
        InputStream stdIn = System.in;
        try {
	        System.setIn(withInput(script));
	        int result = executeCommand("groovy", "=");
	
	        promise.waitSafely(Max_Wait_Time);
	        
	        return result;
        } finally {
        	System.setIn(stdIn);
        }
    }

    private int executeCommand(String... args) {
        //TODO use RealJenkinsRule
        //return executor.call(args).execute(noManualInput(), info(logger), error(logger));
        try {
        	List<String> cliArgs = new ArrayList<>(List.of("-s", jenkinsUrl.toString(), "-http"));
        	cliArgs.addAll(List.of(args));
        	
        	return CLI._main(cliArgs.toArray(new String[0]));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Couldn't connect to Jenkins at '%s'", jenkinsUrl), e);
        }
    }

    private InputStream withInput(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }
}
