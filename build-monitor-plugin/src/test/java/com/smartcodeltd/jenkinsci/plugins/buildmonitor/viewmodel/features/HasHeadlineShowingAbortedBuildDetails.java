package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import hudson.model.User;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class HasHeadlineShowingAbortedBuildDetails {

    private JobView view;

    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @Before
    public void setup() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::getInstance).thenReturn(jenkins);
    }

    @After
    public void tearDown() {
        mockedJenkins.close();
    }

    @Test
    public void should_tell_who_aborted_the_build() throws Exception {
        try (MockedStatic<User> mockedUser = mockStatic(User.class)) {
            view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                    a(job().whereTheLast(build().wasAbortedBy("Abe", mockedUser)))));

            assertThat(headlineOf(view), is("Execution aborted by Abe"));
        }
    }

    @Test
    public void should_tell_if_a_build_was_aborted() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasAbortedBy("Abe", null)))));

        assertThat(headlineOf(view), is("Execution aborted"));
    }

    // --

    private Feature hasHeadlineThatShowsCommitters() {
        return new HasHeadline(new HeadlineConfig(true));
    }

    private Feature hasHeadlineThatDoesNotShowCommitters() {
        return new HasHeadline(new HeadlineConfig(false));
    }

    private String headlineOf(JobView job) {
        return job.which(HasHeadline.class).asJson().value();
    }
}
