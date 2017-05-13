package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import hudson.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ User.class })
public class HasHeadlineShowingAbortedBuildDetails {

    private JobView view;

    @Test
    public void should_tell_who_aborted_the_build() throws Exception {

        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasAbortedBy("Abe")))));

        assertThat(headlineOf(view), is("Execution aborted by Abe"));
    }

    @Test
    public void should_tell_if_a_build_was_aborted() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasAbortedBy("Abe")))));

        assertThat(headlineOf(view), is("Execution aborted"));
    }

    // --

    private Feature hasHeadlineThatShowsCommitters() {
        return new HasHeadline(new HeadlineConfig(true, false));
    }

    private Feature hasHeadlineThatDoesNotShowCommitters() {
        return new HasHeadline(new HeadlineConfig(false, false));
    }

    private String headlineOf(JobView job) {
        return job.which(HasHeadline.class).asJson().value();
    }
}
