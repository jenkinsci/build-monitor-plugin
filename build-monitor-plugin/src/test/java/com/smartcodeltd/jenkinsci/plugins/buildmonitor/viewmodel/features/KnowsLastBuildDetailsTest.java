package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.junit.Test;

import java.util.Date;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.TimeMachine.assumeThat;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.TimeMachine.currentTime;
import static hudson.model.Result.SUCCESS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KnowsLastBuildDetailsTest {
    private JobView view;

    @Test
    public void should_know_current_build_number() {
        view = a(jobView().which(new KnowsLastBuildDetails()).of(
                a(job().whereTheLast(build().hasNumber(5)))));

        assertThat(lastBuildOf(view).name(), is("#5"));
    }

    @Test
    public void should_use_build_name_if_its_known() {
        view = a(jobView().which(new KnowsLastBuildDetails()).of(
                a(job().whereTheLast(build().hasName("1.3.4+build.15")))));

        assertThat(lastBuildOf(view).name(), is("1.3.4+build.15"));
    }

    @Test
    public void should_know_the_url_of_the_last_build() {
        view = a(jobView().which(new KnowsLastBuildDetails()).of(
                a(job().whereTheLast(build().hasNumber(22))))
                .with(locatedAt("job/project-name")));

        assertThat(lastBuildOf(view).url(), is("job/project-name/22/"));
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

        view = a(jobView().which(new KnowsLastBuildDetails()).of(
                a(job().whereTheLast(build().startedAt(startTime).isStillBuilding())))
                .assuming(currentTime));
        assumeThat(currentTime).is(sixSecondsLater);
        assertThat(lastBuildOf(view).duration(), is("6s"));

        assumeThat(currentTime).is(twoAndHalfMinutesLater);
        assertThat(lastBuildOf(view).duration(), is("2m 30s"));

        assumeThat(currentTime).is(anHourAndHalfLater);
        assertThat(lastBuildOf(view).duration(), is("1h 30m 0s"));
    }

    @Test
    public void should_know_how_long_the_last_build_took_once_its_finished() throws Exception {
        view = a(jobView().which(new KnowsLastBuildDetails()).of(
                a(job().whereTheLast(build().finishedWith(SUCCESS).and().took(3)))));

        assertThat(lastBuildOf(view).duration(), is("3m 0s"));
    }

    @Test
    public void should_not_say_anything_about_the_duration_if_the_build_hasnt_run_yet() throws Exception {
        view = a(jobView().which(new KnowsLastBuildDetails()).of(a(job())));

        assertThat(lastBuildOf(view).duration(), is(""));
    }

    /*
     * Last build, last success and last failure (ISO 8601)
     */
    @Test
    public void should_know_how_long_since_the_last_build_happened() throws Exception {
        String tenMinutesInMilliseconds = String.format("%d", 10 * 60 * 1000);

        view = a(jobView().which(new KnowsLastBuildDetails()).of(
                a(job().whereTheLast(build().startedAt("18:05:00").and().took(5))))
                .assuming(currentTime().is("18:20:00")));

        assertThat(lastBuildOf(view).timeElapsedSince(), is(tenMinutesInMilliseconds));
    }

    private KnowsLastBuildDetails.LastBuild lastBuildOf(JobView job) {
        return job.which(KnowsLastBuildDetails.class).asJson();
    }
}
