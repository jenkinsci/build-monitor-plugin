package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HasHeadlineWhichShowsCommittersTest {
    private JobView view;

    @Test
    public void should_say_nothing_if_no_builds_were_executed() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().thatHasNeverRun())));

        assertThat(headlineOf(view), isEmptyString());
    }

    @Test
    public void should_say_nothing_if_no_builds_were_executed_and_one_is_running_now() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().isStillBuilding()))));

        assertThat(headlineOf(view), isEmptyString());
    }

    @Test
    public void should_tell_whose_changes_are_being_built() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().isStillBuilding().withChangesFrom("Adam")))));

        assertThat(headlineOf(view), is("Building Adam's changes"));
    }

    @Test
    public void should_tell_whose_changes_are_being_built_when_there_are_multiple_committers() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().isStillBuilding().withChangesFrom("Ben", "Adam")))));

        assertThat(headlineOf(view), is("Building Adam and Ben's changes"));
    }

    @Test
    public void should_tell_who_broke_the_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")))));

        assertThat(headlineOf(view), is("1 build has failed since Adam committed their changes"));
    }

    @Test
    public void should_list_committers_who_broke_the_build_in_alphabetical_order() throws Exception {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam", "Ben")))));

        assertThat(headlineOf(view), is("1 build has failed since Adam and Ben committed their changes"));
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

        assertThat(headlineOf(view), is("Back in the green!"));
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
        return new HasHeadline(new HeadlineConfig(true));
    }

    private String headlineOf(JobView job) {
        return job.which(HasHeadline.class).asJson().value();
    }
}
