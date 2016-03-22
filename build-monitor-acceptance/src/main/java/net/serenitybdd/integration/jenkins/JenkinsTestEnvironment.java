package net.serenitybdd.integration.jenkins;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;
import net.serenitybdd.integration.jenkins.environment.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.JenkinsTestEnvironmentDetails;
import net.serenitybdd.integration.jenkins.environment.PluginDescriber;
import net.serenitybdd.integration.jenkins.environment.PluginDescription;
import org.junit.rules.TestRule;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.getProperty;
import static net.serenitybdd.integration.utils.Nulls.getOrElse;

public class JenkinsTestEnvironment {

    public static JenkinsTestEnvironment forPluginAt(String pathToPluginUnderTest) {
        return new JenkinsTestEnvironment(cwd.resolve(pathToPluginUnderTest));
    }

    public JenkinsTestEnvironment usingTemporaryDirectoryAt(String pathToTemporaryDirectory) {
        this.tempDir = cwd.resolve(pathToTemporaryDirectory);

        return this;
    }

    public JenkinsTestEnvironment with(PluginDescriber... describers) {
        for (PluginDescriber describer : describers) {
            testRules.add(describer.describing(pluginUnderTest));
        }

        return this;
    }

    public JenkinsInstance create() {
        return new JenkinsInstance(environmentDetails, ImmutableList.copyOf(testRules));
    }

    // --

    private JenkinsTestEnvironment(Path pathToPluginUnderTest) {
        this.pluginUnderTest    = PluginDescription.of(pathToPluginUnderTest);
        this.environmentDetails = new JenkinsTestEnvironmentDetails(pluginUnderTest, tempDir);
    }

    private static final Path cwd     = Paths.get(getOrElse(getProperty("project.root"), getProperty("user.dir")));

    private final JenkinsTestEnvironmentDetails environmentDetails;
    private final PluginDescription pluginUnderTest;
    private final List<TestRule>    testRules = Lists.newArrayList();
    private       Path              tempDir   = Paths.get(getProperty("java.io.tmpdir"));
}
