package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Job;

import java.util.Arrays;
import java.util.List;

public abstract class BaseFeature<JSON extends Object> implements Feature<JSON> {
    private JobView job;

    @Override
    public BaseFeature of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public JSON asJson() {
        return null;
    }

    @Override
    public List<JobView> jobs(JobView parentJobView, Job<?, ?> parentJob) {
        /* Can override this if a feature can create multiple jobviews */
        return Arrays.asList();
    }
}
