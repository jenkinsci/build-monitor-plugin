package net.serenitybdd.integration.jenkins.environment;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class PluginDescription {
    public static PluginDescription of(@NotNull Path pluginAtPath) {
        try {
            Attributes attrs = new JarFile(pluginAtPath.toFile()).getManifest().getMainAttributes();

            return new PluginDescription(
                    pluginAtPath,
                    attrs.getValue("Long-Name"),
                    attrs.getValue("Plugin-Version"),
                    attrs.getValue("Jenkins-Version")
            );
        }
        catch(IOException e) {
            throw new RuntimeException(String.format("Couldn't read the manifest file of '%s'.", pluginAtPath.toAbsolutePath()), e);
        }
    }

    public PluginDescription(@NotNull Path pathToPluginUnderTest, @NotNull String fullName, @NotNull String version, @NotNull String requiredJenkinsVersion) {
        this.path = pathToPluginUnderTest;
        this.fullName = fullName;
        this.version = version;
        this.requiredJenkinsVersion = requiredJenkinsVersion;
    }

    public Path path() {
        return path;
    }

    public String fullName() {
        return fullName;
    }

    public String version() {
        return version;
    }

    public String requiredJenkinsVersion() {
        return requiredJenkinsVersion;
    }

    private final Path path;
    private final String fullName;
    private final String version;
    private final String requiredJenkinsVersion;
}
