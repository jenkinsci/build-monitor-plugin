package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HasHeadlineWhichDoesNotShowCommittersTest {
    private JobView view;

    @Test
    public void should_tell_whose_changes_are_being_built() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().isStillBuilding().withChangesFrom("Adam")))));

        assertThat(headlineOf(view), isEmptyString());
    }

    @Test
    public void should_not_tell_who_broke_the_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")))));

        assertThat(headlineOf(view), is("1 build has failed"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_broken_build() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Ben", "Connor")).
                        andThePrevious(build().wasBrokenBy("Daniel")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("3 builds have failed"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_build_broken_by_multiple_committers() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben", "Connor")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("2 builds have failed"));
    }

    @Test
    public void should_congratulate_anonymously_when_the_build_is_fixed() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().succeededThanksTo("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Back in the green!"));
    }

// --

    private Feature hasHeadlineThatDoesNotShowCommitters() {
        return new HasHeadline(new HeadlineConfig(false));
    }

    private String headlineOf(JobView job) {
        return job.which(HasHeadline.class).asJson().value();
    }
}