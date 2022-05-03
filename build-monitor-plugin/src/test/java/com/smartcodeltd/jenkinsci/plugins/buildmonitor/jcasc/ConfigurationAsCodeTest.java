package com.smartcodeltd.jenkinsci.plugins.buildmonitor.jcasc;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import hudson.model.View;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.model.CNode;
import java.util.Collection;
import jenkins.model.Jenkins;
import org.junit.ClassRule;
import org.junit.Test;

import static io.jenkins.plugins.casc.misc.Util.getJenkinsRoot;
import static io.jenkins.plugins.casc.misc.Util.toStringFromYamlFile;
import static io.jenkins.plugins.casc.misc.Util.toYamlString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ConfigurationAsCodeTest {

    @ClassRule
    @ConfiguredWithCode("configuration-as-code.yml")
    public static JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();

    @Test
    public void should_support_configuration_as_code() {
        Collection<View> views = Jenkins.get().getViews();

        assertThat(views.size(), is(1));

        BuildMonitorView view = (BuildMonitorView) views.iterator().next();
        assertThat(view.getTitle(), is("My Monitor"));
        assertThat(view.getIncludeRegex(), is(".+\\/(my-job-.*)\\/(master|demo)"));
        assertThat(view.getViewName(), is("My-Monitor"));
        assertThat(view.isRecurse(), is(true));
    }

    @Test
    public void should_support_configuration_export() throws Exception {
        ConfiguratorRegistry registry = ConfiguratorRegistry.get();
        ConfigurationContext context = new ConfigurationContext(registry);
        CNode yourAttribute = getJenkinsRoot(context).get("views");

        String exported = toYamlString(yourAttribute);

        String expected = toStringFromYamlFile(this, "configuration-as-code-expected.yml");

        assertThat(exported, is(expected));
    }
}
