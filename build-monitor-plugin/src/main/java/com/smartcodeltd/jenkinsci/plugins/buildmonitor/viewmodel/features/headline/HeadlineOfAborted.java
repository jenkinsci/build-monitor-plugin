package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Result;

/**
 * @author Jan Molak
 */
public class HeadlineOfAborted implements CandidateHeadline {
    public HeadlineOfAborted(JobView job, HeadlineConfig config) {
        super();
    }

    @Override
    public boolean isApplicableTo(JobView job) {
        return Result.ABORTED.equals(job.lastBuild().result());
    }

    @Override
    public Headline asJson() {
        return new Headline("Execution aborted");
    }
}
