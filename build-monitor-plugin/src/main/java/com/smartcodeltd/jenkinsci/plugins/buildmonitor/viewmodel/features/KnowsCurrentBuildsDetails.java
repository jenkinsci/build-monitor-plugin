package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;

import static com.google.common.collect.Lists.newArrayList;

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
            return "[" + Joiner.on(", ").join(stages) + "]";
        }
        return "";
    }

    public static class CurrentBuilds {
        private final List<CurrentBuild> builds = newArrayList();

        public CurrentBuilds(List<BuildViewModel> currentBuilds) {
        	for (Iterator<BuildViewModel> i = currentBuilds.iterator(); i.hasNext(); ) {
        		builds.add(new CurrentBuild(i.next()));
        	}
        }

		@JsonValue
		public List<CurrentBuild> value() {
			return ImmutableList.copyOf(builds);
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
        public final String status() {
            return build.status();
        }
    }
}
