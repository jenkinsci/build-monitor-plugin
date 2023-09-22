package net.serenitybdd.integration.jenkins.process;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsProcess {
    private static final Logger Log = LoggerFactory.getLogger(JenkinsProcess.class);
    private static final int Startup_Timeout = 5 * 60 * 1000;
    public static final String JENKINS_IS_FULLY_UP_AND_RUNNING = "Jenkins is fully up and running";

    private final ProcessBuilder process;
    private final int port;
    private Process jenkinsProcess;
    private final Thread shutdownHook = new Thread() {
        @Override
        public void run() {
            if (jenkinsProcess.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (jenkinsProcess.isAlive()) {
                    jenkinsProcess.destroyForcibly();
                }
            }
        }
    };

    private JenkinsLogWatcher jenkinsLogWatcher;
    private Thread jenkinsLogWatcherThread;

    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "FindBugs does not like the JAVA_HOME resolve")
    public JenkinsProcess(@NonNull Path java, @NonNull Path jenkinsWar, @NonNull int port, @NonNull Path jenkinsHome) {
        Log.debug("jenkins.war:  {}", jenkinsWar.toAbsolutePath());
        Log.debug("JENKINS_HOME: {}", jenkinsHome.toAbsolutePath());

        this.port = port;

        Map<String, String> env = Map.of(
                "JENKINS_HOME", jenkinsHome.toAbsolutePath().toString(),
                "JAVA_HOME", java.getParent().getParent().toAbsolutePath().toString());

        process = process(
                        java,
                        "-Duser.language=en",
                        "-Dhudson.Main.development=true",
                        "-Dorg.slf4j.simpleLogger.defaultLogLevel=debug",
                        "-Djava.util.logging=DEBUG",
                        "-Dhudson.DNSMultiCast.disabled=true",
                        "-jar",
                        jenkinsWar.toString(),
                        "--httpPort=" + port,
                        "--enable-future-java")
                .directory(jenkinsHome.toFile());

        process.environment().putAll(env);
        process.redirectErrorStream(true);
    }

    public void start() throws IOException {
        jenkinsProcess = start(process);
        jenkinsLogWatcher = new JenkinsLogWatcher(jenkinsProcess.getInputStream());
        jenkinsLogWatcherThread = new Thread(jenkinsLogWatcher, "jenkins");

        jenkinsLogWatcherThread.start();

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        Promise<Matcher, ?, ?> portConflictDetected =
                jenkinsLogWatcher.watchFor("java.net.BindException: Address already in use");
        Promise<Matcher, ?, ?> jenkinsStarted = jenkinsLogWatcher.watchFor(JENKINS_IS_FULLY_UP_AND_RUNNING);

        try {
            jenkinsStarted.waitSafely(Startup_Timeout);

            if (!jenkinsStarted.isResolved()) {
                throw new RuntimeException(String.format(
                        "Jenkins failed to start within %s seconds, aborting the test.", Startup_Timeout));
            }

            Log.info("Jenkins is now available at http://localhost:{}", port);
        } catch (InterruptedException e) {
            throw portConflictDetected.isResolved()
                    ? new RuntimeException(
                            String.format("Couldn't start Jenkins on port '%s', the port is already in use", port), e)
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
            throw new RuntimeException(
                    String.format("Did not see '%s' in the Jenkins log within %s ms", logLine, Startup_Timeout), e);
        }
    }

    public void stop() {
        Log.info("Stopping Jenkins...");
        jenkinsProcess.destroy();

        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        Log.info("Jenkins stopped");
    }

    private ProcessBuilder process(Path executable, String... arguments) {
        List<String> args = new ArrayList<>(windowsOrUnix(executable));
        args.addAll(List.of(arguments));

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
        return OS.contains("win") ? List.of("cmd.exe", "/C", command.toString()) : List.of(command.toString());
    }

    public JenkinsLogWatcher getJenkinsLogWatcher() {
        return jenkinsLogWatcher;
    }
}
