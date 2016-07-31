package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;
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
        BuildViewModel lastBuild = job.lastBuild();
        if (SUCCESS.equals(lastBuild.result())) {
            return successHeadline(lastBuild);
        } else {
            return failureHeadline(lastBuild);
        }
    }

    private String successHeadline(BuildViewModel lastBuild) {
        Optional<BuildViewModel> lastCompletedBuildBeforeThisOne = buildBefore(lastBuild);

        if (!lastCompletedBuildBeforeThisOne.isPresent() || SUCCESS.equals(lastCompletedBuildBeforeThisOne.get().result())) {
            return "";
        } else {
            return Lister.describe(
                    "",
                    "Succeeded after %s committed their changes :-)",
                    newLinkedList(committersOf(lastBuild))
            );
        }
    }

    // todo: clean up
    private Optional<BuildViewModel> buildBefore(BuildViewModel build) {
        BuildViewModel previousBuild = build;
        do {
            if (previousBuild.hasPreviousBuild()) {
                previousBuild = previousBuild.previousBuild();
            } else {
                return Optional.absent();
            }
        } while (previousBuild.isRunning());

        return Optional.of(previousBuild);
    }

    private String failureHeadline(BuildViewModel lastBuild) {
        List<BuildViewModel> failedBuildsNewestToOldest = failedBuildsSince(lastBuild);

        BuildViewModel firstFailedBuild = Iterables.getLast(failedBuildsNewestToOldest);

        switch (failedBuildsNewestToOldest.size()) {
            case 0:         // todo: is this case needed?
                return "";

            case 1:
                return Lister.describe(
                            "",
                            "Failed after %s committed their changes",
                            newLinkedList(committersOf(firstFailedBuild))
                        );

            default:
                String buildsFailedSoFar = Pluraliser.pluralise(
                        "%s build has failed",
                        "%s builds have failed",
                        failedBuildsNewestToOldest.size() - 1
                );

                return Lister.describe(
                        buildsFailedSoFar,
                        buildsFailedSoFar + " since %s committed their changes",
                        newLinkedList(committersOf(firstFailedBuild))
                );
        }
    }

    private Set<String> committersOf(BuildViewModel build) {
        return config.displayCommitters
                ? build.culprits()
                : ImmutableSet.<String>of();
    }

    private List<BuildViewModel> failedBuildsSince(BuildViewModel build) {
        BuildViewModel currentBuild = build;

        List<BuildViewModel> failedBuilds = Lists.newArrayList();

        while (! SUCCESS.equals(currentBuild.result())) {

            if (! currentBuild.isRunning()) {
                failedBuilds.add(currentBuild);
            }

            if (! currentBuild.hasPreviousBuild()) {
                break;
            }

            currentBuild = currentBuild.previousBuild();
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
