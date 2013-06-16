package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.Job;
import hudson.model.Result;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static hudson.model.Result.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JobViewTest {

    private static final String theName = "Test Job Name";
    private JobView view;

    @Test
    public void shouldKnowTheNameOfTheJobItsBasedOn() {
        view = JobView.of(a(job().withName(theName)));

        assertThat(view.name(), is(theName));
    }
    
    /* 
     * Should produce a meaningful status description that can be used in the CSS
     */

    @Test
    public void shouldDescribeTheJobAsSuccessfulIfTheLastBuildSucceeded() {
        view = JobView.of(a(job().whereTheLastBuildResultIs(SUCCESS)));

        assertThat(view.status(), containsString("successful"));
    }

    @Test
    public void shouldDescribeTheJobAsFailingIfItTheLastBuildFailed() {
        for (Result result : asFollows(FAILURE, ABORTED, NOT_BUILT, UNSTABLE)) {
            view = JobView.of(a(job().whereTheLastBuildResultIs(result)));

            assertThat(view.status(), containsString("failing"));
        }
    }

    @Test
    public void shouldDescribeTheJobAsRunningIfItIsRunning() {
        List<JobView> views = asFollows(
                JobView.of(a(job().thatHasntStartedYet())),
                JobView.of(a(job().thatIsStillBuilding())),
                JobView.of(a(job().thatIsStillUpdatingTheLog()))
        );

        for (JobView view : views) {
            assertThat(view.status(), containsString("running"));
        }
    }

    @Test
    public void shouldDescribeTheJobAsRunningAndSuccessfulIfItIsRunningAndThePreviousBuildSucceeded() {
        List<JobView> views = asFollows(
                JobView.of(a(job().
                        thatHasntStartedYet().
                        whereThePreviousBuildResultIs(SUCCESS))),

                JobView.of(a(job().
                        thatIsStillBuilding().
                        whereThePreviousBuildResultIs(SUCCESS))),

                JobView.of(a(job().
                        thatIsStillUpdatingTheLog().
                        whereThePreviousBuildResultIs(SUCCESS)))
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
                        thatHasntStartedYet().
                        whereThePreviousBuildResultIs(FAILURE))),

                JobView.of(a(job().
                        thatIsStillBuilding().
                        whereThePreviousBuildResultIs(FAILURE))),

                JobView.of(a(job().
                        thatIsStillUpdatingTheLog().
                        whereThePreviousBuildResultIs(FAILURE)))
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
        view = JobView.of(a(job().
                whereTheLastBuildWasBrokenBy("Adam", "Ben")));

        assertThat(view.culprits(), hasSize(2));
        assertThat(view.culprits(), hasItems("Adam", "Ben"));
    }

    @Test
    public void shouldKnowWhoHasBeenCommittingOverBrokenBuild() {
        view = JobView.of(a(job().
                whereTheLastBuildWasBrokenBy("Adam").
                andThePreviousBuildWasBrokenBy("Ben", "Connor").
                andThePreviousBuildWasBrokenBy("Daniel").
                andThePreviousBuildSucceededThanksTo("Erol")));

        assertThat(view.culprits(), hasSize(4));
        assertThat(view.culprits(), hasItems("Adam", "Ben", "Connor", "Daniel"));
        assertThat(view.culprits(), not(hasItem("Erol")));
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

    private <T> List<T> asFollows(T... examples) {
        return Arrays.asList(examples);
    }
}