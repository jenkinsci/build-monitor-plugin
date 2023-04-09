package net.serenitybdd.integration.jenkins.environment.rules;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstallPluginsFromDisk implements ApplicativeTestRule<JenkinsInstance> {
    private static final Logger Log = LoggerFactory.getLogger(InstallPluginsFromDisk.class);

    private final List<String> pluginIDs;
    private final List<Path> pluginsToInstall;

    public InstallPluginsFromDisk(Path... pluginsToInstall) {
        this.pluginIDs = List.of();
        this.pluginsToInstall = List.of(pluginsToInstall);
    }

    @Override
    public TestRule applyTo(final JenkinsInstance jenkins) {
        return new TestWatcher() {
            @Override
            protected void starting(Description description) {
                Path pluginsDir = jenkins.home().resolve("plugins");
                Log.info(
                        "Installing {} into {}",
                        pluginsToInstall.stream().map(Object::toString).collect(Collectors.joining(", ")),
                        pluginsDir);
                copyPlugins(pluginsToInstall, pluginsDir);
            }

            @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", justification = "TODO needs triage")
            protected void copyPlugins(List<Path> plugins, Path pluginsDir) {
                try {
                    Files.createDirectories(pluginsDir);

                    for (Path plugin : plugins) {
                        Files.copy(
                                existing(plugin),
                                pluginsDir.resolve(
                                        plugin.getFileName().toString().replace(".hpi", ".jpi")),
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(
                            String.format("Couldn't install '%s' under '%s'", plugins, pluginsDir.toAbsolutePath()));
                }
            }

            private Path existing(Path plugin) {
                if (!Files.exists(plugin)) {
                    throw new IllegalArgumentException(
                            String.format("Plugin file '%s' doesn't exist and couldn't be installed.", plugin));
                }

                return plugin;
            }
        };
    }
}
