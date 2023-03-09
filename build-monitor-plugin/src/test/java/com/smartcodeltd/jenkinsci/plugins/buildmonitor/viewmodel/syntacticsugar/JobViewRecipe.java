package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.Feature;
import hudson.model.Job;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class JobViewRecipe implements Supplier<JobView> {
    private Job<?, ?> job;
    private RelativeLocation relative;
    private Date systemTime = new Date();
    private List<Feature> features = new ArrayList<>();
    private boolean isPipeline;

    public JobViewRecipe of(Job<?, ?> job) {
        this.job = job;
        this.relative = RelativeLocation.of(job); // default
        return this;
    }

    public JobViewRecipe which(Feature... features) {
        this.features = List.of(features);
        return this;
    }

    public JobViewRecipe isAPipeline() {
        this.isPipeline = true;
        return this;
    }

    // todo: should RelativeLocation be a Feature?
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
        return new JobView(job, features, isPipeline, relative, systemTime);
    }
}