package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HasHeadlineShowingFixedBuildDetailsTest {

    private JobView view;

    @Test
    public void should_tell_who_fixed_the_broken_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Fixed after Adam committed their changes :-)"));
    }

    @Test
    public void should_list_committers_who_fixed_the_broken_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam", "Connor")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Fixed after Adam and Connor committed their changes :-)"));
    }

    @Test
    public void should_congratulate_anonymously_if_broken_build_was_fixed_without_known_committers() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo()).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is(""));
    }

    @Test
    public void should_congratulate_anonymously_when_the_build_is_fixed_but_the_committers_should_not_be_displayed() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is(""));
    }

    @Test
    public void should_not_congratulate_if_previous_succeeded() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")).
                        andThePrevious(build().succeededThanksTo("Ben")))));

        assertThat(headlineOf(view), isEmptyString());
    }

    @Test
    public void should_not_congratulate_if_no_failure_before() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")))));

        assertThat(headlineOf(view), isEmptyString());
    }

    // --

    private Feature hasHeadlineThatShowsCommitters() {
        return new HasHeadline(new HeadlineConfig(true, true));
    }

    private Feature hasHeadlineThatDoesNotShowCommitters() {
        return new HasHeadline(new HeadlineConfig(false, false));
    }

    private String headlineOf(JobView job) {
        return job.which(HasHeadline.class).asJson().value();
    }
}
