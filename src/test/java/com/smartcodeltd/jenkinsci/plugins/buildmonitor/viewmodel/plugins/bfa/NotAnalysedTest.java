package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa;

import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NotAnalysedTest {
    @Test
    public void should_provide_sensible_defaults_if_the_build_has_no_known_failures_or_failure_plugin_is_not_installed() throws Exception {
        Analysis analysis = new NotAnalysed();

        assertThat(analysis.toString(), is("No known failures"));

        assertThat(analysis.foundKnownFailures(), is(false));
        assertThat(analysis.failures(), is(empty()));
    }
}