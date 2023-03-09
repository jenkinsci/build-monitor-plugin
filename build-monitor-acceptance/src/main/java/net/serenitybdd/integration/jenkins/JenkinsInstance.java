package net.serenitybdd.integration.jenkins;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.serenitybdd.integration.jenkins.client.JenkinsClient;
import net.serenitybdd.integration.jenkins.environment.PluginDescription;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.integration.jenkins.environment.rules.ManageJenkinsServer;
import net.serenitybdd.integration.utils.ListFunctions;
import net.serenitybdd.integration.utils.RuleChains;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class JenkinsInstance implements TestRule {
    private Path jenkinsHome = Paths.get(System.getProperty("java.io.tmpdir"));
    private int  portNumber  = 8080;

    private JenkinsClient client = null;    // instantiated when the Jenkins server is up and running

    private List<ApplicativeTestRule<JenkinsInstance>> customRulesToApplyBeforeStart = new ArrayList<>();
    private List<ApplicativeTestRule<JenkinsInstance>> defaultRules;
    private List<ApplicativeTestRule<JenkinsInstance>> customRulesToApplyAfterStart = new ArrayList<>();

    /**
     * @param descriptions Plugin meta-data derived from the manifest files packaged with the plugins
     */
    public JenkinsInstance(Collection<PluginDescription> descriptions) {
        defaultRules = new ArrayList<>();
        for (PluginDescription description : descriptions) {
            defaultRules.add(InstallPlugins.fromDisk(description.path()));
        }
        defaultRules.add(new ManageJenkinsServer());
    }

    public String version() {
        return System.getProperty("jenkins.version");
    }

    public Path home() {
        return this.jenkinsHome;
    }

    public void setHome(Path jenkinsHome) {
        this.jenkinsHome = jenkinsHome;
    }

    public JenkinsClient client() {
        return client;
    }

    public void setClient(JenkinsClient client) {
        this.client = client;
    }

    public URL url() {
        try {
            return new URL(String.format("http://localhost:%d/", portNumber));
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Couldn't instantiate a URL as 'http://localhost:%d/'", portNumber));
        }
    }

    public int  port() {
        return portNumber;
    }

    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    public <ATR extends ApplicativeTestRule<JenkinsInstance>> JenkinsInstance beforeStartApply(List<ATR> customRulesToBeApplied) {
        this.customRulesToApplyBeforeStart = List.copyOf(customRulesToBeApplied);

        return this;
    }

    public <ATR extends ApplicativeTestRule<JenkinsInstance>> JenkinsInstance afterStartApply(List<ATR> customRulesToBeApplied) {
        this.customRulesToApplyAfterStart = List.copyOf(customRulesToBeApplied);

        return this;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return chainOf(ListFunctions.concat(customRulesToApplyBeforeStart, defaultRules, customRulesToApplyAfterStart)).apply(base, description);
    }

    private <ATR extends ApplicativeTestRule<JenkinsInstance>> RuleChain chainOf(List<ATR> rules) {
        return RuleChains.chained(instantiated(rules));
    }

    private <ATR extends ApplicativeTestRule<JenkinsInstance>> List<TestRule> instantiated(List<ATR> rules) {
        List<TestRule> instantiatedRules = new ArrayList<>(rules.size());

        for (ATR testRule : rules) {
            instantiatedRules.add(testRule.applyTo(this));
        }

        return instantiatedRules;
    }
}
