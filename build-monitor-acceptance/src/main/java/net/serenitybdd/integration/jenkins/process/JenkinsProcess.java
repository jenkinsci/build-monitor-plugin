package net.serenitybdd.integration.jenkins.process;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class JenkinsProcess {
    private static final Logger Log = LoggerFactory.getLogger(JenkinsProcess.class);
    private static final int Startup_Timeout = 5 * 60 * 1000;
    public static final String JENKINS_IS_FULLY_UP_AND_RUNNING = "Jenkins is fully up and running";

    private final ProcessBuilder process;
    private final int port;
    private       Process     jenkinsProcess;
    private final Thread      shutdownHook = new Thread() {
        @Override
        public void run() {
            jenkinsProcess.destroy();
        }
    };

    private JenkinsLogWatcher jenkinsLogWatcher;
    private Thread jenkinsLogWatcherThread;

    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", justification = "FindBugs does not like the JAVA_HOME resolve")
    public JenkinsProcess(@NotNull Path java, @NotNull Path jenkinsWar, @NotNull int port, @NotNull Path jenkinsHome) {
        Log.debug("jenkins.war:  {}", jenkinsWar.toAbsolutePath());
        Log.debug("JENKINS_HOME: {}", jenkinsHome.toAbsolutePath());

        this.port = port;

        Map<String, String> env = new HashMap<>();
        env.put("JENKINS_HOME", jenkinsHome.toAbsolutePath().toString());
        env.put("JAVA_HOME",    java.getParent().getParent().toAbsolutePath().toString());

        List<String> arguments = new ArrayList<>();
        String httpProxy = System.getProperty("http.proxyHost");
        String httpProxyPort = System.getProperty("http.proxyPort");
        String httpProxyUser = System.getProperty("http.proxyUser");
        String httpProxyPassword = System.getProperty("http.proxyPassword");
        String httpsProxy = System.getProperty("https.proxyHost");
        String httpsProxyPort = System.getProperty("https.proxyPort");
        String httpsProxyUser = System.getProperty("https.proxyUser");
        String httpsProxyPassword = System.getProperty("https.proxyPassword");
        if (httpProxy != null) {
            arguments.add("-Dhttp.proxyHost=" + httpProxy);
        }
        if (httpProxyPort != null) {
            arguments.add("-Dhttp.proxyPort=" + httpProxyPort);
        }
        if (httpProxyUser != null) {
            arguments.add("-Dhttp.proxyUser=" + httpProxyUser);
        }
        if (httpProxyPassword != null) {
            arguments.add("-Dhttp.proxyPassword=" + httpProxyPassword);
        }
        if (httpsProxy != null) {
            arguments.add("-Dhttps.proxyHost=" + httpsProxy);
        }
        if (httpsProxyPort != null) {
            arguments.add("-Dhttps.proxyPort=" + httpsProxyPort);
        }
        if (httpsProxyUser != null) {
            arguments.add("-Dhttps.proxyUser=" + httpsProxyUser);
        }
        if (httpsProxyPassword != null) {
            arguments.add("-Dhttps.proxyPassword=" + httpsProxyPassword);
        }
        arguments.addAll(Arrays.asList("-Duser.language=en",
                "-Dhudson.Main.development=true",
                "-Dorg.slf4j.simpleLogger.defaultLogLevel=debug",
                "-Djava.util.logging=DEBUG",
                "-jar", jenkinsWar.toString(),
                "--ajp13Port=-1",
                "--httpPort=" + port));
        process = process(java, arguments).directory(jenkinsHome.toFile());

        process.environment().putAll(Collections.unmodifiableMap(env));
        process.redirectErrorStream(true);
    }

    public void start() throws IOException {
        jenkinsProcess          = start(process);
        jenkinsLogWatcher       = new JenkinsLogWatcher(jenkinsProcess.getInputStream());
        jenkinsLogWatcherThread = new Thread(jenkinsLogWatcher, "jenkins");

        jenkinsLogWatcherThread.start();

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        Promise<Matcher, ?, ?> portConflictDetected = jenkinsLogWatcher.watchFor("java.net.BindException: Address already in use");
        Promise<Matcher, ?, ?> jenkinsStarted       = jenkinsLogWatcher.watchFor(JENKINS_IS_FULLY_UP_AND_RUNNING);

        try {
            jenkinsStarted.waitSafely(Startup_Timeout);

            if (! jenkinsStarted.isResolved()) {
                throw new RuntimeException(format("Jenkins failed to start within %s seconds, aborting the test.", Startup_Timeout));
            }

            Log.info("Jenkins is now available at http://localhost:{}", port);
        } catch (InterruptedException e) {
            throw portConflictDetected.isResolved()
                    ? new RuntimeException(format("Couldn't start Jenkins on port '%s', the port is already in use", port), e)
                    : new RuntimeException("Couldn't start Jenkins", e);
        }
    }

    public Promise<Matcher, ?, ?> promiseWhen(String logLine) {
        return jenkinsLogWatcher.watchFor(logLine);
    }

    public void waitUntil(String logLine) {
        try {
            jenkinsLogWatcher.watchFor(logLine).waitSafely(Startup_Timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(format("Did not see '%s' in the Jenkins log within %s ms", logLine, Startup_Timeout), e);
        }
    }

    public void stop() {
        Log.info("Stopping Jenkins...");
        jenkinsProcess.destroy();

        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        Log.info("Jenkins stopped");
    }

    private ProcessBuilder process(Path executable, List<String> arguments) {
        List<String> args = new ArrayList<>(windowsOrUnix(executable));
        args.addAll(arguments);

        return new ProcessBuilder(args);
    }

    private Process start(ProcessBuilder jenkinsProcessBuilder) throws IOException {
        Log.info("Starting Jenkins on port {}...", port);

        Process startedProcess = jenkinsProcessBuilder.start();
        startedProcess.getOutputStream().close();

        return startedProcess;
    }

    private static String OS = System.getProperty("os.name").toLowerCase();

    private List<String> windowsOrUnix(Path command) {
        return OS.contains("win")
                ? asList("cmd.exe", "/C", command.toString())
                : asList(command.toString());
    }

    public JenkinsLogWatcher getJenkinsLogWatcher() {
        return jenkinsLogWatcher;
    }
}
