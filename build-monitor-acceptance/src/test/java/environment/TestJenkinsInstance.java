package environment;

import net.serenitybdd.integration.browserstack.BrowserStackTestNameSetter;
import net.serenitybdd.integration.jenkins.JenkinsTestEnvironment;

public class TestJenkinsInstance {
    public static JenkinsTestEnvironment withBuildMonitor() {
        return JenkinsTestEnvironment
                .forPluginAt("build-monitor-plugin/target/build-monitor-plugin.hpi")
                .usingTemporaryDirectoryAt("build-monitor-acceptance/target/jenkins")
                .with(new BrowserStackTestNameSetter());
    }
}
