package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import org.jenkinsci.plugins.junitrealtimetestreporter.AbstractRealtimeTestResultAction;
import org.jenkinsci.plugins.junitrealtimetestreporter.TestProgress;

/**
 * @author Daniel Beland
 */
public class HasJunitRealtime implements Feature<HasJunitRealtime.RealtimeTests> {
    private ActionFilter filter = new ActionFilter();
    private JobView job;

    public HasJunitRealtime() {}

    @Override
    public HasJunitRealtime of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public RealtimeTests asJson() {
        List<BuildViewModel> builds = job.currentBuilds();
        if (builds.isEmpty()) {
            return null;
        }

        BuildViewModel build = builds.get(0);
        Iterator<AbstractRealtimeTestResultAction> details =
                build.allDetailsOf(AbstractRealtimeTestResultAction.class).stream()
                        .filter(filter)
                        .iterator();

        return details.hasNext() ? new RealtimeTests(details) : null; // `null` because we don't want to serialise an
        // empty object
    }

    public static class RealtimeTests {
        private final List<RealtimeTest> realtimeTests = new ArrayList<>();

        public RealtimeTests(Iterator<AbstractRealtimeTestResultAction> realtimeTestResultAction) {
            while (realtimeTestResultAction.hasNext()) {
                AbstractRealtimeTestResultAction action = realtimeTestResultAction.next();
                realtimeTests.add(new RealtimeTest(action.getTestProgress(), action.getFailCount()));
            }
        }

        @JsonValue
        public List<RealtimeTest> value() {
            return List.copyOf(realtimeTests);
        }
    }

    public static class RealtimeTest {
        private final TestProgress testProgress;
        private final int failCount;

        public RealtimeTest(TestProgress testProgress, int failCount) {
            this.testProgress = testProgress;
            this.failCount = failCount;
        }

        @JsonProperty
        public String getEstimatedRemainingTime() {
            return testProgress.getEstimatedRemainingTime();
        }

        @JsonProperty
        public int[] getCompletedPercentages() {
            int value1 =
                    Math.min(testProgress.getCompletedTestsPercentage(), testProgress.getCompletedTimePercentage());
            int value2 =
                    Math.max(testProgress.getCompletedTestsPercentage(), testProgress.getCompletedTimePercentage());

            return new int[] {value1, value2 - value1};
        }

        @JsonProperty
        public int getCompletedTests() {
            return testProgress.getCompletedTests();
        }

        @JsonProperty
        public int getExpectedTests() {
            return testProgress.getExpectedTests();
        }

        @JsonProperty
        public String getStyle() {
            if (failCount > 0) {
                return "red";
            }

            return "";
        }
    }

    private static class ActionFilter implements Predicate<AbstractRealtimeTestResultAction> {
        @Override
        public boolean test(AbstractRealtimeTestResultAction action) {
            // Need to trigger the polling manually first
            action.getResult();
            return action.getTestProgress() != null;
        }
    }
}
