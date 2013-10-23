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
        // todo: consider introducing a BuildResultJudge to keep this logic in one place
        switch (lastCompletedBuild().result().ordinal) {
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

        if (lastBuild().isRunning()) {
            status += " running";
        }

        return status;
    }

    @JsonProperty
    public String lastBuildName() {
        return lastBuild().name();
    }

    @JsonProperty
    public String lastBuildUrl() {
        return lastBuild().url();
    }

    @JsonProperty
    public String lastBuildDuration() {
        if (lastBuild().isRunning()) {
            return formatted(lastBuild().elapsedTime());
        }

        return formatted(lastBuild().duration());
    }

    @JsonProperty
    public String estimatedDuration() {
        return formatted(lastBuild().estimatedDuration());
    }

    private String formatted(Duration duration) {
        return null != duration
                ? duration.toString()
                : "";
    }

    @JsonProperty
    public int progress() {
        return lastBuild().progress();
    }

    @JsonProperty
    public Set<String> culprits() {
        Set<String> culprits = new HashSet<String>();

        BuildViewModel build = lastBuild();
        // todo: consider introducing a BuildResultJudge to keep this logic in one place
        while (! SUCCESS.equals(build.result())) {
            culprits.addAll(build.culprits());

            if (! build.hasPreviousBuild()) {
                break;
            }

            build = build.previousBuild();
        }

        return culprits;
    }

    public String toString() {
        return name();
    }


    private JobView(Job<?, ?> job, Date systemTime) {
        this.job = job;
        this.systemTime = systemTime;
    }

    private BuildViewModel lastBuild() {
        return buildViewOf(job.getLastBuild());
    }

    private BuildViewModel lastCompletedBuild() {
        BuildViewModel previousBuild = lastBuild();
        while (previousBuild.isRunning() && previousBuild.hasPreviousBuild()) {
            previousBuild = previousBuild.previousBuild();
        }

        return previousBuild;
    }

    private BuildViewModel buildViewOf(Run<?, ?> build) {
        if (null == build) {
            return new NullBuildView();
        }

        return BuildView.of(job.getLastBuild(), systemTime);
    }
}