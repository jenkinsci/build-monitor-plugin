package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static hudson.model.Result.*;

public class HeadlineOfFixed implements CandidateHeadline {

    private final JobView job;
    private final HeadlineConfig config;

    public HeadlineOfFixed(JobView job, HeadlineConfig config) {
        this.job = job;
        this.config = config;
    }

    @Override
    public boolean isApplicableTo(JobView job) {
        return didTheJobJustGetFixedWith(job.lastCompletedBuild());
    }

    @Override
    public Headline asJson() {
        return new Headline(textFor(job.lastCompletedBuild()));
    }

    private String textFor(BuildViewModel lastBuild) {

        return Lister.describe(
                "Back in the green!",
                "Fixed after %s committed their changes :-)",
                new LinkedList<>(committersOf(lastBuild))
        );
    }

    private boolean didTheJobJustGetFixedWith(BuildViewModel build) {
        return SUCCESS.equals(build.result()) && previousFailed(build);
    }

    private boolean previousFailed(BuildViewModel build) {
        return build.hasPreviousBuild() &&
                (FAILURE.equals(build.previousBuild().result()) || UNSTABLE.equals(build.previousBuild().result()) || ABORTED.equals(build.previousBuild().result()));
    }

    private Set<String> committersOf(BuildViewModel build) {
        return config.displayCommitters
                ? build.committers()
                : new HashSet<>();
    }
}
