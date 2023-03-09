package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class HasHeadlineShowingFailedBuildDetailsTest {

    private JobView view;

    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @Before
    public void setup() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::get).thenReturn(jenkins);
    }

    @After
    public void tearDown() {
        mockedJenkins.close();
    }

    @Test
    public void should_tell_who_broke_the_build() {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")))));

        assertThat(headlineOf(view), is("1 build has failed since Adam committed their changes"));
    }

    @Test
    public void should_list_committers_who_broke_the_build_in_alphabetical_order() {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam", "Ben")))));

        assertThat(headlineOf(view), is("1 build has failed since Adam and Ben committed their changes"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_broken_build_and_the_author_of_the_first_offending_commit() {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Ben", "Connor")).
                        andThePrevious(build().wasBrokenBy("Daniel")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("3 builds have failed since Daniel committed their changes"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_build_broken_by_multiple_committers() {
        view = a(jobView().which(hasHeadlineThatShowsCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben", "Connor")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("2 builds have failed since Ben, Connor and Daniel committed their changes"));
    }

    @Test
    public void should_tell_how_many_builds_have_failed_but_not_who_broke_them_if_configured_as_such() {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")))));

        assertThat(headlineOf(view), is("1 build has failed"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_broken_build() {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Ben", "Connor")).
                        andThePrevious(build().wasBrokenBy("Daniel")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("3 builds have failed"));
    }

    @Test
    public void should_tell_the_number_of_broken_builds_since_the_last_build_broken_when_multiple_committers_are_involver() {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().wasBrokenBy("Adam")).
                        andThePrevious(build().wasBrokenBy("Daniel", "Ben", "Connor")).
                        andThePrevious(build().succeededThanksTo("Errol")))));

        assertThat(headlineOf(view), is("2 builds have failed"));
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
