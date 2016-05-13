package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.Job;

import java.util.Date;

public class JobViewRecipe implements Supplier<JobView> {
    private Job<?, ?> job;
    private Config config = Config.defaultConfig();
    private BuildAugmentor augmentor = new BuildAugmentor();
    private RelativeLocation relative;
    private Date systemTime = new Date();
    private int pipelineId = 1;
    private boolean groupPipeline = false;

    public JobViewRecipe of(Job<?, ?> job) {
        this.job = job;
        this.relative = RelativeLocation.of(job); // default
        return this;
    }

    public JobViewRecipe configured(Config config) {
        this.config = config;
        return this;
    }

    public JobViewRecipe augmented(BuildAugmentor augmentor) {
        this.augmentor = augmentor;
        return this;
    }

    public JobViewRecipe with(RelativeLocation relative) {
        this.relative = relative;
        return this;
    }

    public JobViewRecipe assuming(Date systemTime) {
        this.systemTime = systemTime;
        return this;
    }

    @Override
    public JobView get() {
        return new JobView(job, config, augmentor, relative, systemTime, pipelineId, groupPipeline);
    }
}