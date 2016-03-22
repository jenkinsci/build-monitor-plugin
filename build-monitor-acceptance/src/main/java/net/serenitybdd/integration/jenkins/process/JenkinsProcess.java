package net.serenitybdd.integration.jenkins.process;

import org.apache.commons.io.input.TeeInputStream;
import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class JenkinsProcess {
    private final static Logger Log = LoggerFactory.getLogger(JenkinsProcess.class);


    private final ProcessBuilder process;
    private final int port;
    private final Path jenkinsRunLog;
    private       Process     jenkinsProcess;
    private final Thread      shutdownHook = new Thread() {
        @Override
        public void run() {
            jenkinsProcess.destroy();
            jenkinsLogWatcher.close();
        }
    };
    private final static int Startup_Timeout = 30 * 1000;

    private JenkinsLogWatcher jenkinsLogWatcher;
    private Thread jenkinsLogWatcherThread;

    public JenkinsProcess(@NotNull Path java, @NotNull Path jenkinsWar, @NotNull int port, @NotNull Path jenkinsHome, @NotNull Path log) {
        Log.debug("jenkins.war:  {}", jenkinsWar.toAbsolutePath());
        Log.debug("JENKINS_HOME: {}", jenkinsHome.toAbsolutePath());
        Log.debug("jenkins log:  {}", log.toAbsolutePath());

        this.port = port;
        this.jenkinsRunLog = log;

        Map<String, String> env = new HashMap<>();
        env.put("JENKINS_HOME", jenkinsHome.toAbsolutePath().toString());
        env.put("JAVA_HOME",    java.getParent().getParent().toAbsolutePath().toString());

        process = process(java,
                "-Duser.language=en",
                "-jar", jenkinsWar.toString(),
                "--ajp13Port=-1",
                "--httpPort=" + port
        ).directory(jenkinsHome.toFile());

        process.environment().putAll(Collections.unmodifiableMap(env));
        process.redirectErrorStream(true);
    }

    public int port() {
        return port;
    }

    // todo: CHECK: does Jenkins process stop when it's restarted?   !!!!
    public void start() throws IOException {
        Log.info("Starting Jenkins on port {}", port);

        jenkinsProcess          = start(process);
        jenkinsLogWatcher       = new JenkinsLogWatcher(tee(jenkinsProcess, jenkinsRunLog));
        jenkinsLogWatcherThread = new Thread(jenkinsLogWatcher, "Jenkins Log Watcher");

        jenkinsLogWatcherThread.start();

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        Promise<Matcher, ?, ?> portConflictDetected = jenkinsLogWatcher.watchFor("java.net.BindException: Address already in use");
        Promise<Matcher, ?, ?> jenkinsStarted       = jenkinsLogWatcher.watchFor("Jenkins is fully up and running");

        try {
            Log.info("Waiting for Jenkins to get ready ...", port);

            jenkinsStarted.waitSafely(Startup_Timeout);

            Log.info("Jenkins is now available at http://localhost:{}", port);
        } catch (InterruptedException e) {
            throw portConflictDetected.isResolved()
                    ? new RuntimeException(format("Couldn't start Jenkins on port '%s', the port is already in use", port), e)
                    : new RuntimeException("Couldn't start Jenkins", e);
        }
    }

    private InputStream tee(Process process, Path log) {
        try {
            return new TeeInputStream(process.getInputStream(), new FileOutputStream(log.toFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(format("Log file not found: '%s'", log.toAbsolutePath()), e);
        }
    }

    public void stop() {
        Log.info("Stopping Jenkins...");
        jenkinsProcess.destroy();
        jenkinsLogWatcher.close();

        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        Log.info("Jenkins stopped");
    }

    private ProcessBuilder process(Path executable, String... arguments) {
        List<String> args = new ArrayList<>(windowsOrUnix(executable));
        args.addAll(asList(arguments));

        return new ProcessBuilder(args);
    }

    private Process start(ProcessBuilder jenkinsProcessBuilder) throws IOException {
        Process process = jenkinsProcessBuilder.start();
        process.getOutputStream().close();

        return process;
    }

    private static String OS = System.getProperty("os.name").toLowerCase();

    private List<String> windowsOrUnix(Path command) {
        return OS.contains("win")
                ? asList("cmd.exe", "/C", command.toString())
                : asList(command.toString());
    }
}
