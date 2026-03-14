package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.locatedAt;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.TimeMachine.currentTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Result;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class KnowsLastCompletedBuildDetailsTest {
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
    public void should_know_current_build_number() {
        view = a(jobView().which(new KnowsLastCompletedBuildDetails()).of(a(job().whereTheLast(build().hasNumber(5)))));

        assertThat(lastCompletedBuildOf(view).name(), is("#5"));
    }

    @Test
    public void should_use_build_name_if_its_known() {
        view = a(jobView()
                .which(new KnowsLastCompletedBuildDetails())
                .of(a(job().whereTheLast(build().hasName("1.3.4+build.15")))));

        assertThat(lastCompletedBuildOf(view).name(), is("1.3.4+build.15"));
    }

    @Test
    public void should_know_the_url_of_the_last_build() {
        view = a(jobView()
                .which(new KnowsLastCompletedBuildDetails())
                .of(a(job().whereTheLast(build().hasNumber(22))))
                .with(locatedAt("job/project-name")));

        assertThat(lastCompletedBuildOf(view).url(), is("job/project-name/22/"));
    }

    /*
     * Elapsed time
     */

    @Test
    public void should_know_how_long_the_last_build_took_once_its_finished() {
        view = a(jobView()
                .which(new KnowsLastCompletedBuildDetails())
                .of(a(job().whereTheLast(
                                build().finishedWith(Result.SUCCESS).and().took(3)))));

        assertThat(lastCompletedBuildOf(view).duration(), is("3m 0s"));
    }

    /*
     * Last build, last success and last failure (ISO 8601)
     */
    @Test
    public void should_know_how_long_since_the_last_build_happened() throws Exception {
        String tenMinutesInMilliseconds = String.format("%d", 10 * 60 * 1000);

        view = a(jobView()
                .which(new KnowsLastCompletedBuildDetails())
                .of(a(job().whereTheLast(build().startedAt("18:05:00").and().took(5))))
                .assuming(currentTime().is("18:20:00")));

        assertThat(lastCompletedBuildOf(view).timeElapsedSince(), is(tenMinutesInMilliseconds));
    }

    private KnowsLastCompletedBuildDetails.LastCompletedBuild lastCompletedBuildOf(JobView job) {
        return job.which(KnowsLastCompletedBuildDetails.class).asJson();
    }
}
