package net.serenitybdd.integration.jenkins.environment;

import com.google.common.collect.ImmutableList;
import com.smartcodeltd.aether.ArtifactTransporter;
import net.serenitybdd.integration.jenkins.JenkinsArtifactTransporter;
import net.serenitybdd.integration.utils.RuleChains;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static java.util.Arrays.asList;
import static net.serenitybdd.integration.utils.ListFunctions.concat;

public class JenkinsInstance implements TestRule {
    private static final Logger Log = LoggerFactory.getLogger(JenkinsInstance.class);

    private final JenkinsTestEnvironmentDetails testEnv;
    private final ArtifactTransporter transporter = JenkinsArtifactTransporter.create();

    private final RuleChain rules;

    public JenkinsInstance(JenkinsTestEnvironmentDetails testEnv, ImmutableList<TestRule> customRules) {
        this.testEnv     = testEnv;
        this.rules       = RuleChains.chained(concat(asList(
                new JenkinsServerManager(testEnv, transporter)
        ), customRules));
    }

    // todo: expose
    // - jenkins "cli driver" so that it can be used via the "ability"


    public URI uri() {
        return URI.create("http://localhost:" + testEnv.port());
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return rules.apply(base, description);
    }
}
