package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import hudson.util.FormValidation;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Loops.asFollows;
import static hudson.util.FormValidation.Kind.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuildMonitorViewTest {

    private BuildMonitorView.Descriptor validator;

    @Before
    public void setUp() throws Exception {
        validator = new BuildMonitorView.Descriptor();
    }

    @Test
    public void form_validator_should_allow_valid_reg_ex_specifying_what_jobs_to_include() {
        for (String regex : asFollows(null, "", ".*", "myproject-.*")) {
            assertThat(itShouldAllow(regex), validator.doCheckIncludeRegex(regex).kind, is(OK));
        }
    }

    @Test
    public void form_validator_should_advise_how_a_regex_could_be_improved() {
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