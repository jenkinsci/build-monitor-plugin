package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import jenkins.model.Jenkins;
import org.junit.Test;
import org.mockito.MockedStatic;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class BuildViewTest {

    private static final String theName = "1.5+build.3";

    @Test
    public void should_know_the_name_of_the_job_its_based_on() {
        try (MockedStatic<Jenkins> mockedJenkins = mockStatic(Jenkins.class)) {
            createMockJenkins(mockedJenkins);

            BuildViewModel view = BuildView.of(a(build().hasName(theName)));

            assertThat(view.name(), is(theName));
            assertThat(view.toString(), is(theName));
        }
    }

    private Jenkins createMockJenkins(MockedStatic<Jenkins> mockedJenkins) {
        Jenkins jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::getInstance).thenReturn(jenkins);
        return jenkins;
    }
}