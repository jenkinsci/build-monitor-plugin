package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class HasHeadlineShowingRunningBuildDetailsTest {

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
    public void should_not_tell_whose_changes_are_being_built_if_commiters_are_not_to_be_displayed() throws Exception {
        view = a(jobView().which(hasHeadlineThatDoesNotShowCommitters()).of(
                a(job().whereTheLast(build().isStillBuilding().withChangesFrom("Adam")))));

        assertThat(headlineOf(view), isEmptyString());
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
