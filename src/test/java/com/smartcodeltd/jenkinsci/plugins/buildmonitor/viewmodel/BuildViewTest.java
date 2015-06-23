package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.withDefaultConfig;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuildViewTest {

    private static final String theName = "1.5+build.3";

    private BuildViewModel view;

    @Test
    public void should_know_the_name_of_the_job_its_based_on() {
        view = BuildView.of(a(build().hasName(theName)), withDefaultConfig());

        assertThat(view.name(), is(theName));
        assertThat(view.toString(), is(theName));
    }

}