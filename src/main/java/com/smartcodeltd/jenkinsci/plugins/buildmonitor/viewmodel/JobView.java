package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;

    public static JobView of(Job<?, ?> job) {
        return new JobView(job, new Date());
    }

    public static JobView of(Job<?, ?> job, Date systemTime) {
        return new JobView(job, systemTime);
    }

    @JsonProperty
    public String name() {
        return job.getName();
    }

    @JsonProperty
    public String status() {
        String status = isSuccessful() ? "successful" : "failing";

        if (isRunning()) {
            status += " running";
        }

        return status;
    }

    @JsonProperty
    public Integer buildNumber() {
        if (job.getLastBuild() == null) {
            return null;
        }
        else {
            return job.getLastBuild().getNumber();
        }
    }

    @JsonProperty
    public int progress() {
        if (! isRunning()) {
            return 0;
        }

        final long now      = systemTime.getTime(),
                   duration = now - whenTheLastBuildStarted();

        if (duration > estimatedDuration()) {
            return 100;
        }

        if (estimatedDuration() > 0) {
            return (int) ((float) duration / (float) estimatedDuration() * 100);
        }

        return 100;
    }

    @JsonProperty
    public List<String> culprits() {
        List<String> culprits = new ArrayList();

        Run<?, ?> run = job.getLastBuild();

        while (run != null) {
            if (run instanceof AbstractBuild<?, ?>) {
                AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) run;
                for (User culprit : build.getCulprits()) {
                    culprits.add(culprit.getFullName());
                }
            }
            run = run.getPreviousBuild();

            // only look for culprits in broken builds
            if (run != null && Result.SUCCESS.equals(run.getResult())) {
                run = null;
            }
        }

        return culprits;
    }


    private JobView(Job<?, ?> job, Date systemTime) {
        this.job = job;
        this.systemTime = systemTime;
    }

    private long whenTheLastBuildStarted() {
        return job.getLastBuild().getTimestamp().getTimeInMillis();
    }

    private long estimatedDuration() {
        return job.getLastBuild().getEstimatedDuration();
    }

    private Result lastResult() {
        Run<?, ?> lastBuild = job.getLastBuild();
        if (isRunning()) {
            lastBuild = lastBuild.getPreviousBuild();
        }

        return lastBuild != null
                ? lastBuild.getResult()
                : Result.NOT_BUILT;
    }

    private boolean isSuccessful() {
        return lastResult() == Result.SUCCESS;
    }

    private boolean isRunning() {
        Run<?, ?> lastBuild = job.getLastBuild();

        return (lastBuild != null)
               && (lastBuild.hasntStartedYet() || lastBuild.isBuilding() || lastBuild.isLogUpdated());
    }

    public String toString() {
        return "[ job: '" + name() + "', status: '" + status() + "' ]";
    }
}