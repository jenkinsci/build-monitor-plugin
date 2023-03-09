package net.serenitybdd.integration.jenkins.environment;

import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import net.serenitybdd.integration.jenkins.environment.rules.FindFreePort;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginDescription {
    private static final Logger Log = LoggerFactory.getLogger(FindFreePort.class);

    public static PluginDescription of(@NotNull Path pluginAtPath) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(pluginAtPath.toFile());
            Attributes attrs = jarFile.getManifest().getMainAttributes();

            return new PluginDescription(
                    pluginAtPath,
                    attrs.getValue("Long-Name"),
                    attrs.getValue("Plugin-Version"),
                    attrs.getValue("Jenkins-Version")
            );
        }
        catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't read the manifest file of '%s'.", pluginAtPath.toAbsolutePath()), e);
        }
        finally {
            if (jarFile != null){
                try {
                    jarFile.close();
                } catch (IOException e) {
                    Log.error("error closing jarFile",e);
                }

            }
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
