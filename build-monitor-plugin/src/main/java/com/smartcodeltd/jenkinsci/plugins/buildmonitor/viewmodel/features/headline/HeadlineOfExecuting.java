package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HeadlineOfExecuting implements CandidateHeadline {
    private final JobView job;
    private final HeadlineConfig config;

    public HeadlineOfExecuting(JobView job, HeadlineConfig config) {
        this.job = job;
        this.config = config;
    }

    @Override
    public boolean isApplicableTo(JobView someJob) {
        return someJob.lastBuild().isRunning();
    }

    @Override
    public Headline asJson() {
        return new Headline(text(job.lastBuild()));
    }

    private String text(BuildViewModel build) {
        return Lister.describe(
                "",
                "Building %s's changes",
                new ArrayList<>(committersOf(build))
        );
    }

    private Set<String> committersOf(BuildViewModel build) {
        return config.displayCommitters
                ? build.committers()
                : new HashSet<>();
    }
}
