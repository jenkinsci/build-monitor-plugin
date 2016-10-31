package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import org.junit.Test;

import java.util.List;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Loops.asFollows;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.TimeMachine.currentTime;
import static hudson.model.Result.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Jan Molak
 */
public class JobViewTest {

    private static final String theName = "some-TLAs-followed-by-a-project-name";
    private static final String displayName = "Pretty name that has some actual meaning";

    private RelativeLocation relativeLocation = mock(RelativeLocation.class); // TODO recipe builder
    private JobView view;

    /*
     * By the way, if you were not aware of this: the configuration page of each job has an "Advanced Project Options"
     * section, where you can set a user-friendly "Display Name"
     */
    @Test
    public void delegates_the_process_of_determining_the_relative_job_name() {
        when(relativeLocation.name()).thenReturn(theName);

        view = a(jobView().of(
                a(job().withName(theName))
        ).with(relativeLocation));

        assertThat(view.name(), is(theName));
        assertThat(view.toString(), is(theName));
        verify(relativeLocation, times(2)).name();
    }

    @Test
    public void delegates_the_process_of_determining_the_relative_url() {
        String expectedUrl = "job/" + theName;
        when(relativeLocation.url()).thenReturn(expectedUrl);

        view = a(jobView().of(
                a(job().withName(theName).withDisplayName(displayName)))
                .with(relativeLocation));

        assertThat(view.url(), is(expectedUrl));
        verify(relativeLocation, times(1)).url();
    }

    /*
     * Should be able to measure the progress
     */

    @Test
    public void progress_of_a_not_started_job_should_be_zero() {
        view = a(jobView().of(a(job())));

        assertThat(view.progress(), is(0));
    }

    @Test
    public void progress_of_a_finished_job_should_be_zero() {
        view = a(jobView().of(
                a(job().whereTheLast(build().finishedWith(SUCCESS)))));

        assertThat(view.progress(), is(0));
    }

    @Test
    public void progress_of_a_nearly_finished_job_should_be_100() throws Exception {
        view = a(jobView().of(
                a(job().whereTheLast(build().isStillBuilding().startedAt("12:00:00").and().usuallyTakes(0))))
                .assuming(currentTime().is("12:00:00")));

        assertThat(view.progress(), is(100));
    }

    @Test
    public void progress_of_a_job_thats_taking_longer_than_expected_should_be_100() throws Exception {
        view = a(jobView()
                .of(a(job().whereTheLast(build().isStillBuilding().startedAt("12:00:00").and().usuallyTakes(5))))
                .assuming(currentTime().is("12:20:00")));

        assertThat(view.progress(), is(100));
    }

    @Test
    public void should_calculate_the_progress_of_a_running_job() throws Exception {
        view = a(jobView().of(
                a(job().whereTheLast(build().isStillBuilding().startedAt("13:10:00").and().usuallyTakes(5))))
                .assuming(currentTime().is("13:11:00")));

        assertThat(view.progress(), is(20));
    }

    @Test
    public void should_know_how_long_the_next_build_is_supposed_to_take() throws Exception {
        view = a(jobView().of(
                a(job().whereTheLast(build().finishedWith(SUCCESS).and().usuallyTakes(5)))));

        assertThat(view.estimatedDuration(), is("5m 0s"));
    }

    @Test
    public void should_not_say_anything_if_it_doesnt_know_how_long_the_next_build_is_supposed_to_take() throws Exception {
        view = a(jobView().of(a(job())));

        assertThat(view.estimatedDuration(), is(""));
    }

    /*
     * Should produce a meaningful status description that can be used in the CSS
     */

    @Test
    public void should_describe_the_job_as_successful_if_the_last_build_succeeded() {
        view = a(jobView().of(
                a(job().whereTheLast(build().finishedWith(SUCCESS)))));

        assertThat(view.status(), containsString("successful"));
    }

    @Test
    public void should_describe_the_job_as_failing_if_the_last_build_failed() {
        view = a(jobView().of(
                a(job().whereTheLast(build().finishedWith(FAILURE)))));

        assertThat(view.status(), containsString("failing"));
    }

    @Test
    public void should_describe_the_job_as_aborted_if_the_last_build_was_aborted() {
        view = a(jobView().of(
                a(job().whereTheLast(build().finishedWith(ABORTED)))));

        assertThat(view.status(), containsString("aborted"));
    }

    @Test
    public void should_describe_the_job_as_unstable_if_the_last_build_is_unstable() {
        view = a(jobView().of(
                a(job().whereTheLast(build().finishedWith(UNSTABLE)))));

        assertThat(view.status(), containsString("unstable"));
    }

    @Test
    public void should_describe_the_state_of_the_job_as_unknown_when_it_is_yet_to_be_determined() {
        view = a(jobView().of(a(job())));

        assertThat(view.status(), containsString("unknown"));
    }

    @Test
    public void should_describe_the_job_as_running_if_it_is_running() {
        List<JobView> views = asFollows(
                a(jobView().of(a(job().whereTheLast(build().hasntStartedYet())))),
                a(jobView().of(a(job().whereTheLast(build().isStillBuilding())))),
                a(jobView().of(a(job().whereTheLast(build().isStillUpdatingTheLog())))));

        for (JobView jobView : views) {
            assertThat(jobView.status(), containsString("running"));
        }
    }

    @Test
    public void should_describe_the_job_as_disabled_if_not_buildable() {
        view = a(jobView().of(a(job().thatIsNotBuildable())));

        assertThat(view.status(), containsString("disabled"));
    }

    @Test
    public void should_describe_the_job_as_disabled_and_failing_if_the_last_build_failed_and_the_job_is_disabled() {
        view = a(jobView().of(a(job().thatIsNotBuildable().whereTheLast(build().finishedWith(FAILURE)))));

        assertThat(view.status(), containsString("disabled"));
        assertThat(view.status(), containsString("failing"));
    }

    @Test
    public void should_describe_the_job_as_running_and_successful_if_it_is_running_and_the_previous_build_succeeded() {
        List<JobView> views = asFollows(
                a(jobView().of(a(job().whereTheLast(build().hasntStartedYet()).andThePrevious(build().finishedWith(SUCCESS))))),
                a(jobView().of(a(job().whereTheLast(build().isStillBuilding()).andThePrevious(build().finishedWith(SUCCESS))))),
                a(jobView().of(a(job().whereTheLast(build().isStillUpdatingTheLog()).andThePrevious(build().finishedWith(SUCCESS))))));

        // I could do this instead of having two assertions:
        // assertThat(view.status(), both(containsString("successful")).and(containsString("running")));
        // but then it would require Java 7

        for (JobView jobView : views) {
            assertThat(jobView.status(), containsString("successful"));
            assertThat(jobView.status(), containsString("running"));
        }
    }

    @Test
    public void should_describe_the_job_as_running_and_failing_if_it_is_running_and_the_previous_build_failed() {
        List<JobView> views = asFollows(
                a(jobView().of(a(job().
                        whereTheLast(build().hasntStartedYet()).
                        andThePrevious(build().finishedWith(FAILURE))))),
                a(jobView().of(a(job().
                        whereTheLast(build().isStillBuilding()).
                        andThePrevious(build().finishedWith(FAILURE))))),
                a(jobView().of(a(job().
                        whereTheLast(build().isStillUpdatingTheLog()).
                        andThePrevious(build().finishedWith(FAILURE)))))
        );

        for (JobView jobView : views) {
            assertThat(jobView.status(), containsString("failing"));
            assertThat(jobView.status(), containsString("running"));
        }
    }

    /*
     * Parallel build execution handling
     */

    @Test
    public void should_describe_the_job_as_successful_when_there_are_several_builds_running_in_parallel_and_the_last_completed_was_successful() {
        view = a(jobView().of(
                a(job().whereTheLast(build().isStillBuilding()).
                        andThePrevious(build().isStillBuilding()).
                        andThePrevious(build().finishedWith(SUCCESS)))));

        assertThat(view.status(), containsString("successful"));
    }

    @Test
    public void should_describe_the_job_as_failing_when_there_are_several_builds_running_in_parallel_and_the_last_completed_failed() {
        view = a(jobView().of(
                a(job().whereTheLast(build().isStillBuilding()).
                        andThePrevious(build().isStillBuilding()).
                        andThePrevious(build().finishedWith(FAILURE)))));

        assertThat(view.status(), containsString("failing"));
    }

    @Test
    public void public_api_should_return_reasonable_defaults_for_jobs_that_never_run() throws Exception {
        view = a(jobView().of(
                a(job().thatHasNeverRun())));

        assertThat(view.estimatedDuration(), is(""));
        assertThat(view.progress(), is(0));
        assertThat(view.status(), is("unknown"));
    }
}
