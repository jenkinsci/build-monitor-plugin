package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.hasJunitRealtime;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.realtimeTest;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class HasJunitRealtimeTest {
    private JobView job;

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
    public void should_support_jobs_without_realtime_test_actions() {
        job = a(jobView().which(hasJunitRealtime()).of(a(job())));

        assertThat(serialisedRealtimeTestsDetailsOf(job), is(nullValue()));
    }

    @Test
    public void should_ignore_actions_without_test_progress() {
        job = a(jobView().which(hasJunitRealtime()).of(a(
                job().whereTheLast(build().isStillBuilding().hasRealtimeTests(realtimeTest().withoutTestProgress())))));

        assertThat(serialisedRealtimeTestsDetailsOf(job), is(nullValue()));
    }

    @Test
    public void should_show_progress_during_build() {
        job = a(jobView().which(hasJunitRealtime()).of(a(job().whereTheLast(build().isStillBuilding()
                .hasRealtimeTests(realtimeTest().withTestProgress(10, 6, 20, 8, "Remaining time text"))))));

        assertThat(serialisedRealtimeTestsDetailsOf(job).value(), hasSize(1));
    }

    @Test
    public void should_support_multiple_test_actions() {
        job = a(jobView().which(hasJunitRealtime())
                .of(a(job().whereTheLast(build().isStillBuilding().hasRealtimeTests(
                        realtimeTest().withTestProgress(10, 6, 20, 8, "Remaining time text"),
                        realtimeTest().withTestProgress(10, 6, 20, 8, "Remaining time text"),
                        realtimeTest().withTestProgress(10, 6, 20, 8, "Remaining time text"))))));

        assertThat(serialisedRealtimeTestsDetailsOf(job).value(), hasSize(3));
    }

    private HasJunitRealtime.RealtimeTests serialisedRealtimeTestsDetailsOf(JobView job) {
        return job.which(HasJunitRealtime.class).asJson();
    }

}
