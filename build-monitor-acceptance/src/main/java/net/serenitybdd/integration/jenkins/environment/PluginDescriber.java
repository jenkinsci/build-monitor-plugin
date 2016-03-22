package net.serenitybdd.integration.jenkins.environment;

import org.junit.rules.TestRule;

public interface PluginDescriber {
    TestRule describing(PluginDescription plugin);
}
