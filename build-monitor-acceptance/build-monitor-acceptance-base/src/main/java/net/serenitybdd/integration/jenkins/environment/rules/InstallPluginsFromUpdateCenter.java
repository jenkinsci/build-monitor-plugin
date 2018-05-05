package net.serenitybdd.integration.jenkins.environment.rules;

import com.google.common.base.Charsets;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.UpdateCenter;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.Arrays.asList;

public class InstallPluginsFromUpdateCenter implements ApplicativeTestRule<JenkinsInstance> {

    private static final Logger Log = LoggerFactory.getLogger(InstallPluginsFromUpdateCenter.class);

    private final UpdateCenter updateCenter = new UpdateCenter();

    private final List<String> requiredPlugins;

    public InstallPluginsFromUpdateCenter(String... plugins) {
        this.requiredPlugins = asList(plugins);
    }

    @Override
    public TestRule applyTo(final JenkinsInstance jenkins) {
        return new TestWatcher() {
            @Override protected void starting(Description description) {

                warmUpUpdateCenterCacheFor(jenkins);

                jenkins.client().installPlugins(requiredPlugins);
            }
        };
    }

    private void warmUpUpdateCenterCacheFor(JenkinsInstance jenkins) {
        try {
            Log.info("Warming up the Update Center cache for Jenkins '{}'", jenkins.version());

            String json        = updateCenter.jsonFor(jenkins.version());
            Path   destination = Files.createDirectories(jenkins.home().resolve("updates")).resolve("default.json");

            Files.write(destination, json.getBytes(Charsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't warm up the Update Center cache", e);
        }
    }
}
