package net.serenitybdd.integration.jenkins.environment;

import com.google.common.base.Charsets;
import com.smartcodeltd.aether.ArtifactTransporter;
import net.serenitybdd.integration.jenkins.client.JenkinsClient;
import net.serenitybdd.integration.jenkins.client.JenkinsClientExecutor;
import net.serenitybdd.integration.jenkins.process.JenkinsProcess;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JenkinsServerManager {
    private final JenkinsTestEnvironmentDetails testEnv;
    private final ArtifactTransporter transporter;
    private JenkinsClient client;

    public JenkinsServerManager(JenkinsTestEnvironmentDetails testEnv, ArtifactTransporter transporter) {
        this.testEnv = testEnv;
        this.transporter = transporter;
    }

    // todo: extract?
    public TestRule rule() {
        return new TestRule() {
            @Override
            public Statement apply(final Statement base, final Description testDescription) {
                return new Statement() {

                    @Override
                    public void evaluate() throws Throwable {
                        JenkinsProcess jenkinsProcess = jenkinsProcessFor(testDescription);
                        client = new JenkinsClient(new JenkinsClientExecutor(testEnv.url()), jenkinsProcess);

                        try {

                            jenkinsProcess.start();

                            installPluginsIfNeeded(client, testEnv.requiredPlugins());

                            base.evaluate();
                        } finally {
                            jenkinsProcess.stop();
                        }
                    }
                };
            }
        };
    }

    private void installPluginsIfNeeded(JenkinsClient client, List<String> plugins) {
        if (! plugins.isEmpty()) {
            client.installPlugins(plugins);
        }
    }

    private JenkinsProcess jenkinsProcessFor(Description acceptanceTest) {
        try {

            Path jenkinsHome = testEnv.temporaryHomeFor(acceptanceTest);
            Path runLog      = prepareJenkinsRunLogCalledAfter(jenkinsHome);

            Path jsonp       = download(uri("https://updates.jenkins-ci.org/update-center.json?id=default&version=%s", testEnv.requiredJenkinsVersion()));
            String json      = Files.readAllLines(jsonp, Charsets.UTF_8).get(1);
            Files.write(Files.createDirectories(jenkinsHome.resolve("updates")).resolve("default.json"), json.getBytes(Charsets.UTF_8));

            return new JenkinsProcess(testEnv.java(), jenkinsWar(), testEnv.port(), jenkinsHome, runLog);
        } catch (IOException e) {
            throw new RuntimeException("Something didn't work, sorry :-(", e);
        }
    }

    private URI uri(String template, String... params) throws MalformedURLException {
        return URI.create(String.format(template, params));
    }

    private Path download(URI link) throws IOException {
//        Paths.get(link).getFileName();
        Path destination = Files.createTempFile("update-center", "json");

        Files.createDirectories(destination.getParent());

        ReadableByteChannel rbc = Channels.newChannel(link.toURL().openStream());
        FileOutputStream fos = new FileOutputStream(destination.toAbsolutePath().toFile());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        return destination;
    }

    private Path prepareJenkinsRunLogCalledAfter(Path home) throws IOException {
        return Files.createFile(home.getParent().resolve(home.getFileName() + ".log"));
    }

    public Path jenkinsWar() {
        return transporter.get("org.jenkins-ci.main", "jenkins-war", testEnv.requiredJenkinsVersion(), "war");
    }

    public JenkinsClient client() {
        return client;
    }
}
