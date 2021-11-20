package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import hudson.util.FormValidation;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Loops.asFollows;
import static hudson.util.FormValidation.Kind.ERROR;
import static hudson.util.FormValidation.Kind.OK;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BuildMonitorDescriptorTest {

    private BuildMonitorDescriptor validator;

    @Rule public final JenkinsRule jenkins = new JenkinsRule();

    @Before
    public void setUp() throws Exception {
        validator = new BuildMonitorDescriptor();
    }

    @Test
    public void form_validator_should_allow_valid_reg_ex_specifying_what_jobs_to_include() throws Exception {
        for (String regex : asFollows(null, "", ".*", "myproject-.*")) {
            assertThat(itShouldAllow(regex), validator.doCheckIncludeRegex(regex).kind, is(OK));
        }
    }

    @Test
    public void form_validator_should_advise_how_a_regex_could_be_improved() throws Exception {
        FormValidation result = validator.doCheckIncludeRegex(")");

        assertThat(result.kind, is(ERROR));
        assertThat(htmlDecoded(result.getMessage()), containsString("Unmatched closing ')'"));
    }

    private String htmlDecoded(String message) {
        return StringEscapeUtils.unescapeHtml(message);
    }

    private String itShouldAllow(String regex) {
        return "should allow " + regex;
    }
}