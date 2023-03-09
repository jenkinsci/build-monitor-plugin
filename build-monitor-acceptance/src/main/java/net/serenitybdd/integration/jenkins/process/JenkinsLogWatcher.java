package net.serenitybdd.integration.jenkins.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsLogWatcher implements AutoCloseable, Runnable {

    private static final Logger Log = LoggerFactory.getLogger(JenkinsLogWatcher.class);

    private final InputStream jenkinsOutput;
    private final List<JenkinsLogLineWatcher> watchers = new CopyOnWriteArrayList<>();
    private boolean stop = false;

    public JenkinsLogWatcher(InputStream jenkinsOutput) {
        this.jenkinsOutput = jenkinsOutput;
    }

    public Promise<Matcher, ?, ?> watchFor(String patternToMatchAgainstALogLine) {
        JenkinsLogLineWatcher watcher = new JenkinsLogLineWatcher(patternToMatchAgainstALogLine);

        watchers.add(watcher);

        return watcher.promise();
    }

    @Override
    public void close() {
        stop = true;
        watchers.clear();
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(jenkinsOutput, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null && !stop) {

                Log.debug(line);

                for (JenkinsLogLineWatcher watcher : watchers) {
                    if (watcher.matches(line)) {
                        watchers.remove(watcher);
                    }
                }
            }
        } catch (IOException e) {
            if (stop && "Stream closed".equals(e.getMessage())) {
                Log.debug("Jenkins OutputStream was closed, but that was expected since we're stopping the log watcher.");
            } else {
                throw new RuntimeException("Jenkins output stream is already closed", e);
            }
        }
    }
}