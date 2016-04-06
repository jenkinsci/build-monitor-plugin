package net.serenitybdd.integration.jenkins.v2;

import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.TestEnvironment;
import net.serenitybdd.integration.jenkins.environment.CWD;
import net.serenitybdd.integration.jenkins.environment.PluginDescription;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.FindFreePort;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.net.URL;
import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestEnvironmentTest {
    private final static Path cwd = new CWD().asPath();
    PluginDescription pluginUnderTest;

    @Before
    public void setUp() throws Exception {
        pluginUnderTest = mock(PluginDescription.class);

        when(pluginUnderTest.requiredJenkinsVersion()).thenReturn("1.609.3");
    }

    @Test
    public void by_default_jenkins_will_start_on_localhost_8080() throws Exception {
        JenkinsInstance jenkins = new TestEnvironment(new JenkinsInstance(pluginUnderTest)).create();

        assertThat(jenkins.url(), is(new URL("http://localhost:8080/")));
    }

    @Test
    public void default_jenkins_port_can_be_overridden() throws Exception {
        JenkinsInstance jenkins = new TestEnvironment(new JenkinsInstance(pluginUnderTest)).create();
        jenkins.setPort(31337);

        assertThat(jenkins.url(), is(new URL("http://localhost:31337/")));
    }

    @Test
    public void default_port_can_be_overridden_by_custom_test_rules() throws Throwable {
        JenkinsInstance jenkins = new TestEnvironment(new JenkinsInstance(pluginUnderTest))
                .beforeStart(new FindFreePort(8080, 8080))
                .create();

        jenkins.apply(mock(Statement.class), mock(Description.class)).evaluate();

        assertThat(jenkins.url(), is(not(new URL("http://127.0.0.1:8080/"))));
    }

    @Test
    public void installs_plugins() throws Throwable {
        JenkinsInstance jenkins = new TestEnvironment(new JenkinsInstance(pluginUnderTest))
                .beforeStart(new FindFreePort(8080, 8080), InstallPlugins.fromUpdateCenter("claim"))
                .create();

        jenkins.apply(mock(Statement.class), mock(Description.class)).evaluate();

        assertThat(jenkins.url(), is(not(new URL("http://127.0.0.1:8080/"))));
    }

    @Test
    public void test_environment_can_be_augmented_with_custom_test_rules() throws Exception {
        JenkinsInstance jenkins = new TestEnvironment(new JenkinsInstance(pluginUnderTest))
                .beforeStart(new ExampleApplicativeTestRule())
                .create();
    }

    static class ExampleApplicativeTestRule implements ApplicativeTestRule<JenkinsInstance> {
        @Override public TestRule applyTo(JenkinsInstance subject) { return null; }
    }
}