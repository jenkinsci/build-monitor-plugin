package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jan Molak
 */
public class KnowsCurrentBuildsDetails implements Feature<KnowsCurrentBuildsDetails.CurrentBuilds> {
    private JobView job;

    public KnowsCurrentBuildsDetails(/* config */) {
    }

    @Override
    public KnowsCurrentBuildsDetails of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public CurrentBuilds asJson() {
        return new CurrentBuilds(job.currentBuilds());
    }

    private static String formattedDuration(Duration duration) {
        return null != duration
                ? duration.value()
                : "";
    }

    private static String formattedStages(List<String> stages) {
        if (!stages.isEmpty()) {
            return "[" + String.join(", ", stages) + "]";
        }
        return "";
    }

    public static class CurrentBuilds {
        private final List<CurrentBuild> builds = new ArrayList<>();

        public CurrentBuilds(List<BuildViewModel> currentBuilds) {
            for (BuildViewModel currentBuild : currentBuilds) {
                builds.add(new CurrentBuild(currentBuild));
            }
        }

		@JsonValue
		public List<CurrentBuild> value() {
			return Collections.unmodifiableList(new ArrayList<>(builds));
		}
    }

    public static class CurrentBuild {
        private final BuildViewModel build;

        public CurrentBuild(BuildViewModel build) {
            this.build = build;
        }

        @JsonProperty
        public final String name() {
            return build.name();
        }

        @JsonProperty
        public final String url() {
            return build.url();
        }

        @JsonProperty
        public final String duration() {
            return formattedDuration(build.elapsedTime());
        }

        @JsonProperty
        public final String description() {
            return build.description();
        }

        @JsonProperty
        public final String pipelineStages() {
            if (build.isPipeline()) {
                return formattedStages(build.pipelineStages());
            }
            return "";
        }
    }
}
