package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HasHeadlineWhichShowsCommittersTest {
    private JobView view;

    @Test
    public void should_tell_who_broke_the_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")))));

        assertThat(headlineOf(view), is("1 build has failed since Adam committed their changes"));
    }

    @Test
    public void should_say_nothing_if_no_builds_were_executed() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().thatHasNeverRun())));

        assertThat(headlineOf(view), is(""));
    }

    @Test
    public void should_list_committers_who_broke_the_build_in_alphabetical_order() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam", "Ben")))));

        assertThat(headlineOf(view), is("1 build has failed since Adam and Ben committed their changes"));
    }

    @Test
    public void should_tell_who_broke_the_previous_build_if_the_current_one_is_still_running() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().isStillBuilding()).
                        andThePrevious(build().wasBrokenBy("Ben")))));

        assertThat(headlineOf(view), is("1 build has failed since Ben committed their changes"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_broken_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Ben", "Connor")).
                        andThePrevious(build().wasBrokenBy("Daniel")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("3 builds have failed since Daniel committed their changes"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_build_broken_by_multiple_committers() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben", "Connor")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("2 builds have failed since Ben, Connor and Daniel committed their changes"));
    }

    @Test
    public void should_tell_who_fixed_the_broken_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Succeeded after Adam committed their changes :-)"));
    }

    @Test
    public void should_list_committers_who_fixed_the_broken_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam", "Connor")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Succeeded after Adam and Connor committed their changes :-)"));
    }

    @Test
    public void should_not_tell_anything_if_broken_build_was_fixed_without_known_committers() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo()).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is(""));
    }

    @Test
    public void should_not_congratulate_if_previous_succeeded() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")).
                        andThePrevious(build().succeededThanksTo("Ben")))));

        assertThat(headlineOf(view), is(""));
    }

    @Test
    public void should_not_congratulate_if_no_failure_before() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")))));

        assertThat(headlineOf(view), is(""));
    }

    // --

    private Feature hasHeadlineThatShowsCommitters() {
        return new HasHeadline(new HasHeadline.Config(true));
    }

    private String headlineOf(JobView job) {
        return job.which(HasHeadline.class).asJson().value();
    }
}
