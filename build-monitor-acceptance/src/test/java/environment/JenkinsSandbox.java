package environment;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.TestEnvironment;
import net.serenitybdd.integration.jenkins.environment.CWD;
import net.serenitybdd.integration.jenkins.environment.PluginDescription;
import net.serenitybdd.integration.jenkins.environment.rules.FindFreePort;
import net.serenitybdd.integration.jenkins.environment.rules.SandboxJenkinsHome;

public class JenkinsSandbox {
    public static TestEnvironment configure() {
        CWD cwd = CWD.or(System.getProperty("project.root"));

        List<PluginDescription> descriptions = new ArrayList<>();
        descriptions.add(
                PluginDescription.of(
                        cwd.resolve("build-monitor-plugin/target/build-monitor-plugin.hpi")));
        try (Stream<Path> stream =
                Files.list(
                        cwd.resolve(
                                "build-monitor-plugin/target/test-classes/test-dependencies"))) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".hpi"))
                    .map(PluginDescription::of)
                    .forEach(descriptions::add);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new TestEnvironment(new JenkinsInstance(descriptions))
                .beforeStart(
                        FindFreePort.useFreePortFromDynamicRange(),
                        SandboxJenkinsHome.useATemporaryDirectoryUnder(cwd.resolve("build-monitor-acceptance/target/jenkins"))
                );
    }
}
