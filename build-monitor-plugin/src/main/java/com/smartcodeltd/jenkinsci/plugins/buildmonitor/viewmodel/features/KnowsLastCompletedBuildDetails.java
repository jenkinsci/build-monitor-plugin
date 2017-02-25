package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Jan Molak
 */
public class KnowsLastCompletedBuildDetails implements Feature<KnowsLastCompletedBuildDetails.LastCompletedBuild> {
    private JobView job;

    public KnowsLastCompletedBuildDetails(/* config */) {
    }

    @Override
    public KnowsLastCompletedBuildDetails of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public LastCompletedBuild asJson() {
        return new LastCompletedBuild(job.lastCompletedBuild());
    }

    private static String formatted(Duration duration) {
        return null != duration
                ? duration.value()
                : "";
    }

    public static class LastCompletedBuild {
        private final BuildViewModel lastCompletedBuild;

        public LastCompletedBuild(BuildViewModel lastCompletedBuild) {
            this.lastCompletedBuild = lastCompletedBuild;
        }

        @JsonProperty
        public final String name() {
            return lastCompletedBuild.name();
        }

        @JsonProperty
        public final String url() {
            return lastCompletedBuild.url();
        }

        @JsonProperty
        public final String duration() {
            return formatted(lastCompletedBuild.duration());
        }

        @JsonProperty
        public final String description() {
            return lastCompletedBuild.description();
        }

        @JsonProperty
        public final String timeElapsedSince() {
            return formatted(lastCompletedBuild.timeElapsedSince());
        }
    }
}
