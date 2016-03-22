package net.serenitybdd.integration.jenkins.environment;

import com.smartcodeltd.aether.ArtifactTransporter;
import net.serenitybdd.integration.jenkins.process.JenkinsProcess;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JenkinsServerManager implements TestRule {
    private final JenkinsTestEnvironmentDetails testEnv;
    private final ArtifactTransporter transporter;

    public JenkinsServerManager(JenkinsTestEnvironmentDetails testEnv, ArtifactTransporter transporter) {
        this.testEnv = testEnv;
        this.transporter = transporter;
    }

    @Override
    public Statement apply(final Statement base, final Description testDescription) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                JenkinsProcess jenkinsProcess = jenkinsProcessFor(testDescription);

                try {

                    jenkinsProcess.start();

                    // todo: register Jenkins driver

                    base.evaluate();


                } finally {
                    jenkinsProcess.stop();
                }
            }
        };
    }

    private JenkinsProcess jenkinsProcessFor(Description acceptanceTest) {
        try {

            Path jenkinsHome = testEnv.temporaryHomeFor(acceptanceTest);
            Path runLog      = prepareJenkinsRunLogCalledAfter(jenkinsHome);

            return new JenkinsProcess(testEnv.java(), jenkinsWar(), testEnv.port(), jenkinsHome, runLog);
        } catch (IOException e) {
            throw new RuntimeException("Something didn't work, sorry :-(", e);
        }
    }

    private Path prepareJenkinsRunLogCalledAfter(Path home) throws IOException {
        return Files.createFile(home.getParent().resolve(home.getFileName() + ".log"));
    }

    public Path jenkinsWar() {
        return transporter.get("org.jenkins-ci.main", "jenkins-war", testEnv.requiredJenkinsVersion(), "war");
    }
}
