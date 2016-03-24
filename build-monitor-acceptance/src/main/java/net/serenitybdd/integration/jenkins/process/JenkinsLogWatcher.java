package net.serenitybdd.integration.jenkins.process;

import org.jdeferred.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;

class JenkinsLogWatcher implements Closeable, Runnable {

    private final static Logger Log = LoggerFactory.getLogger(JenkinsLogWatcher.class);

    private final InputStream jenkinsOutput;
    private final List<JenkinsLogLineWatcher> watchers = new CopyOnWriteArrayList<>();

    public JenkinsLogWatcher(InputStream jenkinsOutput) {
        this.jenkinsOutput = jenkinsOutput;
    }

    public Promise<Matcher, ?, ?> watchFor(String patternToMatchAgainstALogLine) {
        JenkinsLogLineWatcher watcher = new JenkinsLogLineWatcher(patternToMatchAgainstALogLine);

        watchers.add(watcher);

        return watcher.promise();
    }

    @Override
    public void close()  {
        try {
            jenkinsOutput.close();
        } catch (IOException e) {
            Log.warn("Couldn't close the output stream of the Jenkins process. Looks like it's already closed.", e);
        }
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(jenkinsOutput))) {
            String line;
            while ((line = reader.readLine()) != null) {

                Log.info(line);

                for (JenkinsLogLineWatcher watcher : watchers) {
                    if (watcher.matches(line)) {
                        watchers.remove(watcher);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't work with the Jenkins output stream", e);
        }
    }
}