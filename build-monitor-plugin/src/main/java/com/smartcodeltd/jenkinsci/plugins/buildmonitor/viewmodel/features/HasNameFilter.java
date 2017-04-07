package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.nameFilter.*;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

public class HasNameFilter implements Feature<NameFilter> {
    private JobView job;
    private NameFilterConfig config;

    public HasNameFilter(NameFilterConfig config) {
        this.config = config;
    }

    @Override
    public HasNameFilter of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public NameFilter asJson() {
        NameFilterer nameFilterer = new NameFilterer(job.name());

        nameFilterer = nameFilterer
                .filterPrefix(config.prefix)
                .filterSuffix(config.suffix)
                .filterRegex(config.regex);

        return new NameFilter(nameFilterer.getJobName());
    }

}