package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Result;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class HeadlineOfFailing implements CandidateHeadline {

    private final JobView job;
    private final HeadlineConfig config;

    public HeadlineOfFailing(JobView job, HeadlineConfig config) {
        this.job = job;
        this.config = config;
    }

    @Override
    public boolean isApplicableTo(JobView job) {
        return Result.FAILURE.equals(job.lastCompletedBuild().result()) || Result.UNSTABLE.equals(job.lastCompletedBuild().result());
    }

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
                new LinkedList<>(responsibleFor(firstFailedBuild))
        );
    }

    private static <T> T getLast(List<T> list) {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return list.get(list.size() - 1);
    }

    private List<BuildViewModel> failedBuildsSince(BuildViewModel build) {
        BuildViewModel currentBuild = build;

        List<BuildViewModel> failedBuilds = new ArrayList<>();

        while (! Result.SUCCESS.equals(currentBuild.result())) {

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
                : new HashSet<>();
    }
}
