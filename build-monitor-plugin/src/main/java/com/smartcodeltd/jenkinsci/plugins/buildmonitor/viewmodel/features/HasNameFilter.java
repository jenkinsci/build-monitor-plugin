package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.jobNameFilter.*;

public class HasNameFilter implements Feature<JobNameFilter> {
    private JobView job;
    private JobNameFilterConfig config;

    public HasNameFilter(JobNameFilterConfig config) {
        this.config = config;
    }

    @Override
    public HasNameFilter of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public JobNameFilter asJson() {
        JobNameFilterer nameFilterer = new JobNameFilterer(job.name());

        nameFilterer = nameFilterer
                .filterPrefix(config.prefix)
                .filterSuffix(config.suffix)
                .filterRegex(config.regex);

        return new JobNameFilter(nameFilterer.getJobName());
    }

}