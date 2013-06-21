package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.Job;
import hudson.model.Result;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static hudson.model.Result.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobViewTest {

    private static final String theName = "Test Job Name";
    private JobView view;

    @Test
    public void shouldKnowTheNameOfTheJobItsBasedOn() {
        view = JobView.of(a(job().withName(theName)));

        assertThat(view.name(), is(theName));
    }

    @Test
    public void shouldKnowCurrentBuildNumber() {
        view = JobView.of(a(job().whereTheCurrentBuildNumberIs(5)));

        assertThat(view.buildNumber(), is(5));
    }

    /*
     * Should be able to measure the progress
     */

    @Test
    public void progressOfANotStartedJobShouldBeZero() throws Exception {
        view = JobView.of(a(job()));

        assertThat(view.progress(), is(0));
    }

    @Test
    public void progressOfAFinishedJobShouldBeZero() throws Exception {
        view = JobView.of(a(job().whereTheLast(build().finishedWith(SUCCESS))));

        assertThat(view.progress(), is(0));
    }

    @Test
    public void progressOfANearlyFinishedJobShouldBe100() throws Exception {
        view = JobView.of(
                    a(job().whereTheLast(build().isStillBuilding().startedAt("12:00:00").andIsEstimatedToTake(0))),
                    assumingThatCurrentTimeIs("12:00:00")
        );

        assertThat(view.progress(), is(100));
    }

    @Test
    public void progressOfAJobThatsTakingLongerThanItShouldIs100() throws Exception {
        view = JobView.of(
                a(job().whereTheLast(build().isStillBuilding().startedAt("12:00:00").andIsEstimatedToTake(5))),
                assumingThatCurrentTimeIs("12:20:00")
        );

        assertThat(view.progress(), is(100));
    }

    @Test
    public void shouldCalculateTheProgressOfARunningJob() throws Exception {
        view = JobView.of(
                a(job().whereTheLast(build().isStillBuilding().startedAt("13:10:00").andIsEstimatedToTake(5))),
                assumingThatCurrentTimeIs("13:11:00")
        );

        assertThat(view.progress(), is(20));
    }

    /*
     * Should produce a meaningful status description that can be used in the CSS
     */

    @Test
    public void shouldDescribeTheJobAsSuccessfulIfTheLastBuildSucceeded() {
        view = JobView.of(a(job().whereTheLast(build().finishedWith(SUCCESS))));

        assertThat(view.status(), containsString("successful"));
    }

    @Test
    public void shouldDescribeTheJobAsFailingIfItTheLastBuildFailed() {
        for (Result result : asFollows(FAILURE, ABORTED, NOT_BUILT, UNSTABLE)) {
            view = JobView.of(a(job().whereTheLast(build().finishedWith(result))));

            assertThat(view.status(), containsString("failing"));
        }
    }

    @Test
    public void shouldDescribeTheJobAsRunningIfItIsRunning() {
        List<JobView> views = asFollows(
                JobView.of(a(job().whereTheLast(build().hasntStartedYet()))),
                JobView.of(a(job().whereTheLast(build().isStillBuilding()))),
                JobView.of(a(job().whereTheLast(build().isStillUpdatingTheLog())))
        );

        for (JobView view : views) {
            assertThat(view.status(), containsString("running"));
        }
    }

    @Test
    public void shouldDescribeTheJobAsRunningAndSuccessfulIfItIsRunningAndThePreviousBuildSucceeded() {
        List<JobView> views = asFollows(
                JobView.of(a(job().
                        whereTheLast(build().hasntStartedYet()).
                        andThePrevious(build().finishedWith(SUCCESS)))),

                JobView.of(a(job().
                        whereTheLast(build().isStillBuilding()).
                        andThePrevious(build().finishedWith(SUCCESS)))),

                JobView.of(a(job().
                        whereTheLast(build().isStillUpdatingTheLog()).
                        andThePrevious(build().finishedWith(SUCCESS))))
        );

        for (JobView view : views) {
            assertThat(view.status(), containsString("successful"));
            assertThat(view.status(), containsString("running"));
        }
    }

    @Test
    public void shouldDescribeTheJobAsRunningAndFailingIfItIsRunningAndThePreviousBuildFailed() {
        List<JobView> views = asFollows(
                JobView.of(a(job().
                        whereTheLast(build().hasntStartedYet()).
                        andThePrevious(build().finishedWith(FAILURE)))),

                JobView.of(a(job().
                        whereTheLast(build().isStillBuilding()).
                        andThePrevious(build().finishedWith(FAILURE)))),

                JobView.of(a(job().
                        whereTheLast(build().isStillUpdatingTheLog()).
                        andThePrevious(build().finishedWith(FAILURE))))
        );

        for (JobView view : views) {
            assertThat(view.status(), containsString("failing"));
            assertThat(view.status(), containsString("running"));
        }
    }

    /*
     * Should produce some basic build statistics
     */

    @Test
    public void shouldKnowHowLongTheJobHasBeenFailing() {
        // TODO
    }

    /*
     * Should know who broke the build
     */

    @Test
    public void shouldKnowWhoBrokeTheBuild() {
        view = JobView.of(a(job().whereTheLast(build().wasBrokenBy("Adam", "Ben"))));

        assertThat(view.culprits(), hasSize(2));
        assertThat(view.culprits(), hasItems("Adam", "Ben"));
    }

    @Test
    public void shouldKnowWhoHasBeenCommittingOverBrokenBuild() {
        view = JobView.of(a(job().
                whereTheLast(build().wasBrokenBy("Adam")).
                andThePrevious(build().wasBrokenBy("Ben", "Connor")).
                andThePrevious(build().wasBrokenBy("Daniel")).
                andThePrevious(build().succeededThanksTo("Errol"))));

        assertThat(view.culprits(), hasSize(4));
        assertThat(view.culprits(), hasItems("Adam", "Ben", "Connor", "Daniel"));
        assertThat(view.culprits(), not(hasItem("Errol")));
    }

    /*
     * Syntactic sugar
     */

    private JobStateRecipe job() {
        return new JobStateRecipe();
    }

    private Job<?, ?> a(JobStateRecipe recipe) {
        return recipe.execute();
    }

    private BuildStateRecipe build() {
        return new BuildStateRecipe();
    }

    private Date assumingThatCurrentTimeIs(String currentTime) throws Exception {
        Date currentDate = new SimpleDateFormat("H:m:s").parse(currentTime);

        Date systemTime = mock(Date.class);
        when(systemTime.getTime()).thenReturn(currentDate.getTime());

        return currentDate;
    }

    private <T> List<T> asFollows(T... examples) {
        return Arrays.asList(examples);
    }
}