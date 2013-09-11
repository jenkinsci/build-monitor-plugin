package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import static hudson.model.Result.SUCCESS;

/**
 * @author Jan Molak
 */
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
        return (null != job.getDisplayNameOrNull())
                ? job.getDisplayName()
                : job.getName();
    }

    @JsonProperty
    public String url() {
        return job.getUrl();
    }

    @JsonProperty
    public String status() {
        String status;

        switch (lastResult().ordinal) {
            case 0:
                status = "successful";
                break;
            case 1:
                status = "unstable";
                break;
            case 2:
                status = "failing";
                break;
            default:
                status = "aborted";
                break;
        }

        if (isRunning()) {
            status += " running";
        }

        return status;
    }

    @JsonProperty
    public String buildName() {
        return job.getLastBuild() != null
                ? job.getLastBuild().getDisplayName()
                : null;
    }

    @JsonProperty
    public String buildUrl() {
        return job.getLastBuild() != null
                ? job.getLastBuild().getUrl()
                : null;
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
/*
    @JsonProperty
    public String elapsedTime() {
        if (! isRunning()) {
            return formatTimestamp(lastBuildDuration());
        }

        final long now      = systemTime.getTime(),
                duration = now - whenTheLastBuildStarted();

        return formatTimestamp(duration);
    }
*/
    @JsonProperty
    public Set<String> culprits() {
        Set<String> culprits = new HashSet<String>();

        Run<?, ?> run = job.getLastBuild();

        while (run != null && ! SUCCESS.equals(run.getResult())) {

            if (run instanceof AbstractBuild<?, ?>) {
                AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) run;

                if (! (isRunning(build))) {
                    for (User culprit : build.getCulprits()) {
                        culprits.add(culprit.getFullName());
                    }
                }
            }

            run = run.getPreviousBuild();
        }

        return culprits;
    }

    /*public String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String timestampFormatted = formatter.format(date);
        return timestampFormatted;
    } */

    public String toString() {
        return name();
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
    /*
    private long lastBuildDuration() {
        return job.getLastBuild().getDuration();
    } */

    private Result lastResult() {
        Run<?, ?> lastBuild = job.getLastBuild();
        if (isRunning()) {
            lastBuild = lastBuild.getPreviousBuild();
        }

        return lastBuild != null
                ? lastBuild.getResult()
                : Result.NOT_BUILT;
    }

    private boolean isRunning() {
        return isRunning(job.getLastBuild());
    }

    private boolean isRunning(Run<?, ?> build) {
        return (build != null)
                && (build.hasntStartedYet() || build.isBuilding() || build.isLogUpdated());
    }
}