package com.smartcodeltd.jenkinsci.plugins.buildmonitor.jcasc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.order.ByStatus;

import hudson.model.View;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.misc.Util;
import io.jenkins.plugins.casc.model.CNode;
import jenkins.model.Jenkins;

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
        assertThat(view.getConfig(), notNullValue());
        assertThat(view.getConfig().getOrder(), isA(ByStatus.class));
        assertThat(view.getConfig().getMaxColumns(), is(3));
    }

    @Test
    public void should_support_configuration_export() throws Exception {
        ConfiguratorRegistry registry = ConfiguratorRegistry.get();
        ConfigurationContext context = new ConfigurationContext(registry);
        CNode yourAttribute = Util.getJenkinsRoot(context).get("views");

        String exported = Util.toYamlString(yourAttribute);

        String expected = Util.toStringFromYamlFile(this, "configuration-as-code-expected.yml");

        assertThat(exported, is(expected));
    }
}
