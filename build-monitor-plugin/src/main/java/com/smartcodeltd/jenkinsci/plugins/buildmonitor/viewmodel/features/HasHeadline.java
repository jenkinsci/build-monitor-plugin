package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Lists.reverse;
import static hudson.model.Result.SUCCESS;

/**
 * @author Jan Molak
 */
public class HasHeadline implements Feature {
    private final Config config;
    private JobView job;

    public HasHeadline(Config config) {
        this.config = config;
    }

    @Override
    public HasHeadline of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Headline asJson() {
        return new Headline(headline());
    }

    private String headline() {
        List<BuildViewModel> failedBuildsAsc = reverse(failedBuilds());

        switch (failedBuildsAsc.size()) {
            case 0:
                return "";

            case 1:
                return Lister.describe(
                            "",
                            "Failed after %s committed their changes",
                            newLinkedList(committersOf(failedBuildsAsc.get(0)))
                        );

            default:
                String buildsFailedSoFar = Pluraliser.pluralise(
                        "%s build has failed",
                        "%s builds have failed",
                        failedBuildsAsc.size() - 1
                );

                return Lister.describe(
                        buildsFailedSoFar,
                        buildsFailedSoFar + " since %s committed their changes",
                        newLinkedList(committersOf(failedBuildsAsc.get(0)))
                );
        }
    }

    private Set<String> committersOf(BuildViewModel build) {
        return config.displayCommitters
                ? build.culprits()
                : ImmutableSet.<String>of();
    }

    private List<BuildViewModel> failedBuilds() {
        List<BuildViewModel> failedBuilds = Lists.newArrayList();

        BuildViewModel build = job.lastBuild();
        while (! SUCCESS.equals(build.result())) {

            if (! build.isRunning()) {
                failedBuilds.add(build);
            }

            if (! build.hasPreviousBuild()) {
                break;
            }

            build = build.previousBuild();
        }

        return failedBuilds;
    }

    public static class Config {
        public final boolean displayCommitters;

        public Config(boolean displayCommitters) {
            this.displayCommitters = displayCommitters;
        }
    }

    public static class Headline {
        private final String value;

        public Headline(String value) {
            this.value = value;
        }

        @JsonValue
        public String value() {
            return value;
        }
    }
}
