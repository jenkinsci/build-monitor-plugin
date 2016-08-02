package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Jan Molak
 */
public class KnowsLastBuildDetails implements Feature<KnowsLastBuildDetails.LastBuild> {
    private JobView job;

    public KnowsLastBuildDetails(/* config */) {
    }

    @Override
    public KnowsLastBuildDetails of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public LastBuild asJson() {
        return new LastBuild(job.lastBuild(), job.lastCompletedBuild());
    }

    private static String formatted(Duration duration) {
        return null != duration
                ? duration.toString()
                : "";
    }

    public static class LastBuild {
        private final BuildViewModel lastBuild;
        private final BuildViewModel lastCompletedBuild;

        public LastBuild(BuildViewModel lastBuild, BuildViewModel lastCompletedBuild) {
            this.lastBuild = lastBuild;
            this.lastCompletedBuild = lastCompletedBuild;
        }

        @JsonProperty
        public final String name() {
            return lastBuild.name();
        }

        @JsonProperty
        public final String url() {
            return lastBuild.url();
        }

        @JsonProperty
        public final String duration() {
            if (lastBuild.isRunning()) {
                return formatted(lastBuild.elapsedTime());
            }

            return formatted(lastBuild.duration());
        }

        @JsonProperty
        public final String description() {
            return lastBuild.description();
        }

        @JsonProperty
        public final String timeElapsedSince() {
            return formatted(lastCompletedBuild.timeElapsedSince());
        }
    }
}
