package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Loops.asFollows;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import hudson.util.FormValidation;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class BuildMonitorDescriptorTest {

    private BuildMonitorDescriptor validator;

    @Rule
    public final JenkinsRule jenkins = new JenkinsRule();

    @Before
    public void setUp() {
        validator = new BuildMonitorDescriptor();
    }

    @Test
    public void form_validator_should_allow_valid_reg_ex_specifying_what_jobs_to_include() {
        for (String regex : asFollows(null, "", ".*", "myproject-.*")) {
            assertThat(itShouldAllow(regex), validator.doCheckIncludeRegex(regex).kind, is(FormValidation.Kind.OK));
        }
    }

    @Test
    public void form_validator_should_advise_how_a_regex_could_be_improved() {
        FormValidation result = validator.doCheckIncludeRegex(")");

        assertThat(result.kind, is(FormValidation.Kind.ERROR));
        assertThat(htmlDecoded(result.getMessage()), containsString("Unmatched closing ')'"));
    }

    private String htmlDecoded(String message) {
        return StringEscapeUtils.unescapeHtml(message);
    }

    private String itShouldAllow(String regex) {
        return "should allow " + regex;
    }
}
