package net.serenitybdd.integration.jenkins.environment.rules;

import net.serenitybdd.integration.browserstack.BrowserStackTestSessionName;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import org.junit.rules.TestRule;

public class DescribeBrowserStackSession implements ApplicativeTestRule<JenkinsInstance> {
    public static DescribeBrowserStackSession forCurrentTest() {
        return new DescribeBrowserStackSession();
    }

    @Override
    public TestRule applyTo(JenkinsInstance jenkins) {
        return BrowserStackTestSessionName
                .forProject(jenkins.pluginUnderTestName())
                .andBuild(jenkins.pluginUnderTestVersion());
    }
}
