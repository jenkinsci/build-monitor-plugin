package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.locatedAt;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.TimeMachine.assumeThat;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.TimeMachine.currentTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.KnowsCurrentBuildsDetails.CurrentBuild;
import java.util.Date;
import java.util.List;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class KnowsCurrentBuildsDetailsTest {
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
        view = a(jobView()
                .which(new KnowsCurrentBuildsDetails())
                .of(a(job().whereTheLast(build().isStillBuilding().and().hasNumber(5)))));

        assertThat(currentBuildsOf(view).get(0).name(), is("#5"));
    }

    @Test
    public void should_use_build_name_if_its_known() {
        view = a(jobView()
                .which(new KnowsCurrentBuildsDetails())
                .of(a(job().whereTheLast(build().isStillBuilding().and().hasName("1.3.4+build.15")))));

        assertThat(currentBuildsOf(view).get(0).name(), is("1.3.4+build.15"));
    }

    @Test
    public void should_know_the_url_of_the_last_build() {
        view = a(jobView()
                .which(new KnowsCurrentBuildsDetails())
                .of(a(job().whereTheLast(build().isStillBuilding().and().hasNumber(22))))
                .with(locatedAt("job/project-name")));

        assertThat(currentBuildsOf(view).get(0).url(), is("job/project-name/22/"));
    }

    /*
     * Elapsed time
     */

    @Test
    public void should_know_how_long_a_build_has_been_running_for() throws Exception {

        String startTime = "13:10:00",
                sixSecondsLater = "13:10:06",
                twoAndHalfMinutesLater = "13:12:30",
                anHourAndHalfLater = "14:40:00";
        Date currentTime = currentTime().is(startTime);

        view = a(jobView()
                .which(new KnowsCurrentBuildsDetails())
                .of(a(job().whereTheLast(build().startedAt(startTime).isStillBuilding())))
                .assuming(currentTime));

        CurrentBuild currentBuild = currentBuildsOf(view).get(0);

        assumeThat(currentTime).is(sixSecondsLater);
        assertThat(currentBuild.duration(), is("6s"));

        assumeThat(currentTime).is(twoAndHalfMinutesLater);
        assertThat(currentBuild.duration(), is("2m 30s"));

        assumeThat(currentTime).is(anHourAndHalfLater);
        assertThat(currentBuild.duration(), is("1h 30m 0s"));
    }

    private List<KnowsCurrentBuildsDetails.CurrentBuild> currentBuildsOf(JobView job) {
        return job.which(KnowsCurrentBuildsDetails.class).asJson().value();
    }
}
