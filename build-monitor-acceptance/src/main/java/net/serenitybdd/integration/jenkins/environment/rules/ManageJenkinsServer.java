package net.serenitybdd.integration.jenkins.environment.rules;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.client.JenkinsClient;
import net.serenitybdd.integration.jenkins.process.JenkinsProcess;
import net.serenitybdd.integration.utils.CommandLineTools;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ManageJenkinsServer implements ApplicativeTestRule<JenkinsInstance> {
    private final Path javaExecutable;

    public ManageJenkinsServer() {
        this(CommandLineTools.java());
    }

    public ManageJenkinsServer(Path javaExecutable) {
        this.javaExecutable = javaExecutable;
    }

    @Override
    public TestRule applyTo(final JenkinsInstance jenkins) {
        return new TestWatcher() {
            private JenkinsProcess process;
            private JenkinsClient client;

            @Override
            protected void starting(Description description) {
                process = jenkinsProcessFor(jenkins, warFileOf(jenkins));

                try {
                    process.start();
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't start Jenkins", e);
                }

                client = new JenkinsClient(jenkins.url(), process);
                jenkins.setClient(client);
            }

            @Override
            protected void finished(Description description) {
                client.shutdown();
            }
        };
    }

    private Path warFileOf(JenkinsInstance jenkins) {
        return Paths.get(System.getProperty("settings.localRepository"))
                .resolve("org")
                .resolve("jenkins-ci")
                .resolve("main")
                .resolve("jenkins-war")
                .resolve(jenkins.version())
                .resolve("jenkins-war-" + jenkins.version() + ".war");
    }

    private JenkinsProcess jenkinsProcessFor(JenkinsInstance jenkins, Path war) {
        Path java = CommandLineTools.java();

        return new JenkinsProcess(java, war, jenkins.port(), jenkins.home());
    }
}
