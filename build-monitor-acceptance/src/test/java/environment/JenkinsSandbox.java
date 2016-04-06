package environment;

import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.TestEnvironment;
import net.serenitybdd.integration.jenkins.environment.CWD;
import net.serenitybdd.integration.jenkins.environment.PluginDescription;
import net.serenitybdd.integration.jenkins.environment.rules.DescribeBrowserStackSession;
import net.serenitybdd.integration.jenkins.environment.rules.FindFreePort;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.integration.jenkins.environment.rules.SandboxJenkinsHome;

import static java.lang.System.getProperty;

public class JenkinsSandbox {
    public static TestEnvironment configure() {
        CWD cwd = CWD.or(getProperty("project.root"));

        PluginDescription pluginUnderTest = PluginDescription.of(cwd.resolve("build-monitor-plugin/target/build-monitor-plugin.hpi"));

        return new TestEnvironment(new JenkinsInstance(pluginUnderTest))
                .beforeStart(
                        FindFreePort.useFreePortFromDynamicRange(),
                        SandboxJenkinsHome.useATemporaryDirectoryUnder(cwd.resolve("build-monitor-acceptance/target/jenkins")),
                        DescribeBrowserStackSession.forCurrentTest()
                )
                .afterStart(
                        InstallPlugins.fromUpdateCenter("cloudbees-folder")
                );
    }
}
