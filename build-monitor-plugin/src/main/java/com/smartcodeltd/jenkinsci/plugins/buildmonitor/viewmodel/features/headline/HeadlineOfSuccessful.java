package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.SerialisableAsJsonObjectCalled;

import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;
import static hudson.model.Result.SUCCESS;

public class HeadlineOfSuccessful implements SerialisableAsJsonObjectCalled<Headline> {

    private final static Set<String> Empty_Set = ImmutableSet.of();

    private final JobView job;
    private final HeadlineConfig config;

    public HeadlineOfSuccessful(JobView job, HeadlineConfig config) {
        this.job = job;
        this.config = config;
    }

    @Override
    public Headline asJson() {
        return new Headline(textFor(job.lastCompletedBuild()));
    }

    private String textFor(BuildViewModel lastBuild) {
        Optional<BuildViewModel> previousBuild = buildBefore(lastBuild);

        if (previousBuild.isPresent() && previousBuild.get().result().isWorseThan(SUCCESS)) {
            return Lister.describe(
                    "And we're back in the green!",
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
