package net.serenitybdd.integration.jenkins.environment.rules;

import net.serenitybdd.integration.jenkins.JenkinsInstance;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class InstallPluginsFromDisk implements ApplicativeTestRule<JenkinsInstance> {
    private static final Logger Log = LoggerFactory.getLogger(InstallPluginsFromDisk.class);

    private final Path pluginsCache;
    private final List<String> pluginIDs;
    private final List<Path> pluginsToInstall;

    public InstallPluginsFromDisk(Path... pluginsToInstall) {
        this.pluginsCache = null;
        this.pluginIDs = asList();
        this.pluginsToInstall = asList(pluginsToInstall);
    }

    public InstallPluginsFromDisk(Path pluginsCache, String... pluginIDs) {
        this.pluginsCache = pluginsCache;
        this.pluginIDs = asList(pluginIDs);
        this.pluginsToInstall = asList();
    }

    @Override
    public TestRule applyTo(final JenkinsInstance jenkins) {
        return new TestWatcher() {
            @Override
            protected void starting(Description description) {
                Path pluginsDir = jenkins.home().resolve("plugins");
                List<Path> plugins;
                if (pluginsCache == null) {
                    Log.info("Installing {} into {}", pluginsToInstall.stream().map(Object::toString).collect(Collectors.joining(", ")), pluginsDir);
                    plugins = pluginsToInstall;
                } else {
                    Log.info("Installing plugins {} into {}", pluginIDs, pluginsDir);
                    plugins = getPluginsFromCache();
                }

                copyPlugins(plugins, pluginsDir);
            }

            protected void copyPlugins(List<Path> plugins, Path pluginsDir) {
                try {
                    Files.createDirectories(pluginsDir);

                    for (Path plugin : plugins) {
                        Files.copy(existing(plugin), pluginsDir.resolve(plugin.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(String.format("Couldn't install '%s' under '%s'", plugins, pluginsDir.toAbsolutePath()));
                }
            }

            private Path existing(Path plugin) {
                if (!Files.exists(plugin)) {
                  throw new IllegalArgumentException(String.format("Plugin file '%s' doesn't exist and couldn't be installed.", plugin));
                }

                return plugin;
            }
            
            private List<Path> getPluginsFromCache() {
                List<Path> plugins = new ArrayList<>();
                
                Path jenkinsVersionPluginCache = pluginsCache.resolve(System.getProperty("jenkins.version"));
                
                for (String pluginDir : pluginIDs) {
                    plugins.addAll(getDirectoryPlugins(jenkinsVersionPluginCache.resolve(pluginDir)));
                }
                
                return plugins;
            }
            
            private List<Path> getDirectoryPlugins(Path location) {
                List<Path> plugins = new ArrayList<>();
                
                String[] files = location.toFile().list((dir, name) -> name.endsWith(".jpi") || name.endsWith(".hpi"));
                
                if (files != null ) {
                    for (String file : files ) {
                        plugins.add(location.resolve(file));
                    }
                }
                
                return plugins;
            }
        };
    }
}
