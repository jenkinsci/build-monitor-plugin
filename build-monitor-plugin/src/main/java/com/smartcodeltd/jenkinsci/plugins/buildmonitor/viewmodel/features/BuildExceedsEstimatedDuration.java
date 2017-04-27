package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.NullBuildView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.DurationInMilliseconds;

public class BuildExceedsEstimatedDuration implements Feature<BuildExceeds> {
    private JobView job;
    private double config;

    public BuildExceedsEstimatedDuration(double config) {
        this.config = config;
    }

    @Override
    public BuildExceedsEstimatedDuration of(JobView jobView) {
        this.job = jobView;
        return this;
    }

    public boolean buildExceedsEstimatedDuration() {
        double overTimeFactor = this.config;

        if (job.lastBuild() instanceof NullBuildView) {
            return false;
        }

        Duration tooLongDuration = new DurationInMilliseconds((long) ((double) job.lastBuild().estimatedDuration().toLong() * overTimeFactor));

        return job.isRunning() && job.lastBuild().elapsedTime().greaterThan(tooLongDuration);
    }

    @Override
    public BuildExceeds asJson() {
        return new BuildExceeds(buildExceedsEstimatedDuration());
    }

}
