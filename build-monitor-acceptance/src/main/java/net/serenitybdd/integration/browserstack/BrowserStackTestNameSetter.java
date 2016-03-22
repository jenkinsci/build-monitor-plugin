package net.serenitybdd.integration.browserstack;

import net.serenitybdd.integration.jenkins.environment.PluginDescriber;
import net.serenitybdd.integration.jenkins.environment.PluginDescription;
import org.junit.rules.TestRule;

public class BrowserStackTestNameSetter implements PluginDescriber {
    @Override
    public TestRule describing(PluginDescription plugin) {
        return DescribeBrowserStackTestSession.forProject(plugin.fullName()).andBuild(plugin.version());
    }
}
