package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class HasHeadlineShowingFixedBuildDetailsTest {

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
    public void should_tell_who_fixed_the_broken_build() {
        view = a(jobView()
                .which(hasHeadlineThatShowsCommitters())
                .of(a(job().whereTheLast(build().succeededThanksTo("Adam"))
                        .andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Fixed after Adam committed their changes :-)"));
    }

    @Test
    public void should_list_committers_who_fixed_the_broken_build() {
        view = a(jobView()
                .which(hasHeadlineThatShowsCommitters())
                .of(a(job().whereTheLast(build().succeededThanksTo("Adam", "Connor"))
                        .andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Fixed after Adam and Connor committed their changes :-)"));
    }

    @Test
    public void should_congratulate_anonymously_if_broken_build_was_fixed_without_known_committers() {
        view = a(jobView()
                .which(hasHeadlineThatShowsCommitters())
                .of(a(job().whereTheLast(build().succeededThanksTo())
                        .andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Back in the green!"));
    }

    @Test
    public void should_congratulate_anonymously_when_the_build_is_fixed_but_the_committers_should_not_be_displayed() {
        view = a(jobView()
                .which(hasHeadlineThatDoesNotShowCommitters())
                .of(a(job().whereTheLast(build().succeededThanksTo("Adam"))
                        .andThePrevious(build().wasBrokenBy("Daniel", "Ben")))));

        assertThat(headlineOf(view), is("Back in the green!"));
    }

    @Test
    public void should_not_congratulate_if_previous_succeeded() {
        view = a(jobView()
                .which(hasHeadlineThatShowsCommitters())
                .of(a(job().whereTheLast(build().succeededThanksTo("Adam"))
                        .andThePrevious(build().succeededThanksTo("Ben")))));

        assertThat(headlineOf(view), is(emptyString()));
    }

    @Test
    public void should_not_congratulate_if_no_failure_before() {
        view = a(jobView()
                .which(hasHeadlineThatShowsCommitters())
                .of(a(job().whereTheLast(build().succeededThanksTo("Adam")))));

        assertThat(headlineOf(view), is(emptyString()));
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
