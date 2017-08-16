package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixProject;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static hudson.Util.filter;

public class ShowMatrixConfigurations<JSON extends Object> extends BaseFeature<JSON> {
    private JobView job;

    @Override
    public ShowMatrixConfigurations of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public List<JobView> jobs(JobView parentJobView, Job<?, ?> parentJob) {
        List<JobView> subJobs = new ArrayList<JobView>();

        List<MatrixConfiguration> matrixConfigurations = parentJob instanceof MatrixProject
                ? filter(parentJob.getAllJobs(), MatrixConfiguration.class)
                : new ArrayList<MatrixConfiguration>();

        for (Job<?, ?> matrixConfiguration : matrixConfigurations) {
            subJobs.add(new JobView(matrixConfiguration, parentJobView.features(), false, RelativeLocation.of(matrixConfiguration), new Date()));
        }

        return subJobs;
    }
}
