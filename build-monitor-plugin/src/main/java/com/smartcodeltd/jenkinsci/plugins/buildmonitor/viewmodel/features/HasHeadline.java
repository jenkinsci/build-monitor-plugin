package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newLinkedList;
import static hudson.model.Result.NOT_BUILT;
import static hudson.model.Result.SUCCESS;

/**
 * @author Jan Molak
 */
public class HasHeadline implements Feature<HasHeadline.Headline> {
    private final static Set<String> Empty_Set = ImmutableSet.of();

    private final Config config;
    private JobView job;

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
        return headlineOf(job).asJson();
    }

    private SerialisableAsJsonObjectCalled<Headline> headlineOf(JobView job) {
        BuildViewModel latestBuild = job.lastBuild();

        if (latestBuild.isRunning()) {
            return new NoHeadline();    // todo: ideally show who's triggered the build, or the current stage of the pipeline, etc.
        }

        if (SUCCESS.equals(latestBuild.result())) {
            return new SuccessHeadline();
        }

        if (NOT_BUILT.equals(latestBuild.result())) {
            return new NoHeadline();
        }

        return new FailureHeadline();
    }

    // --

    private class NoHeadline implements SerialisableAsJsonObjectCalled<Headline> {

        @Override
        public Headline asJson() {
            return new Headline("");
        }
    }

    private class SuccessHeadline implements SerialisableAsJsonObjectCalled<Headline> {

        @Override
        public Headline asJson() {
            return new Headline(textFor(job.lastCompletedBuild()));
        }

        private String textFor(BuildViewModel lastBuild) {
            Optional<BuildViewModel> previousBuild = buildBefore(lastBuild);

            if (previousBuild.isPresent() && previousBuild.get().result().isWorseThan(SUCCESS)) {
                return Lister.describe(
                        "",
                        "Succeeded after %s committed their changes :-)",
                        newLinkedList(committersOf(lastBuild))
                );
            } else {
                return "";
            }
        }

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

        private Set<String> committersOf(BuildViewModel build) {
            return config.displayCommitters
                    ? build.committers()
                    : Empty_Set;
        }
    }

    private class FailureHeadline implements SerialisableAsJsonObjectCalled<Headline> {

        @Override
        public Headline asJson() {
            return new Headline(text(job.lastCompletedBuild()));
        }

        private String text(BuildViewModel lastBuild) {
            List<BuildViewModel> failedBuildsNewestToOldest = failedBuildsSince(lastBuild);

            String buildsFailedSoFar = Pluraliser.pluralise(
                    "%s build has failed",
                    "%s builds have failed",
                    failedBuildsNewestToOldest.size()
            );

            BuildViewModel firstFailedBuild = failedBuildsNewestToOldest.isEmpty()
                    ? lastBuild
                    : getLast(failedBuildsNewestToOldest);

            return Lister.describe(
                    buildsFailedSoFar,
                    buildsFailedSoFar + " since %s committed their changes",
                    newLinkedList(responsibleFor(firstFailedBuild))
            );
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

        private Set<String> responsibleFor(BuildViewModel build) {
            return config.displayCommitters
                    ? build.culprits()
                    : Empty_Set;
        }
    }
}
