package net.serenitybdd.integration.jenkins.environment;

import net.serenitybdd.integration.utils.CommandLineTools;
import org.junit.runner.Description;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class JenkinsTestEnvironmentDetails {

    private final PluginDescription pluginUnderTest;
    private final Path tempDir;

    public JenkinsTestEnvironmentDetails(PluginDescription pluginUnderTest, Path tempDir) {
        checkArgument(Files.exists(pluginUnderTest.path()), "Location of the plugin file doesn't seem to be correct: %s", pluginUnderTest.path().toAbsolutePath());

        this.pluginUnderTest = pluginUnderTest;
        this.tempDir         = tempDir;
    }

    public String requiredJenkinsVersion() {
        return pluginUnderTest.requiredJenkinsVersion();
    }

    public int port() {
        return 8080;
    }

    public Path temporaryHomeFor(Description test) {
        try {
            Path home = temporaryJenkinsHome(tempDir, fileSystemSafeNameOf(test));

            copy(pluginUnderTest, Files.createDirectories(home.resolve("plugins")));

            return home;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't create a temporary Jenkins home at '%s'.", tempDir), e);
        }
    }

    public Path java() {
        return CommandLineTools.java();
    }

    // --

    private Path temporaryJenkinsHome(Path root, String testName) throws IOException {
        return Files.createTempDirectory(
                Files.createDirectories(root),
                testName + "_"
        );
    }


    private String fileSystemSafeNameOf(Description description) {
        return format("%s_%s",
                simplified(description.getClassName()),
                description.getMethodName()
        );
    }

    private String simplified(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    private Path copy(PluginDescription plugin, Path plugins) throws IOException {
        return Files.copy(plugin.path(), plugins.resolve(plugin.path().getFileName()));
    }
}
