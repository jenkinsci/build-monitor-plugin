package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

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
        	for (Iterator<BuildViewModel> i = currentBuilds.iterator(); i.hasNext(); ) {
        		builds.add(new CurrentBuild(i.next()));
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
