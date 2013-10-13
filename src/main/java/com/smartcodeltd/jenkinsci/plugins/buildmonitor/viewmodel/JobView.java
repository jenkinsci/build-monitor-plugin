package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

import static hudson.model.Result.NOT_BUILT;
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
        String status = isSuccessful() ? "successful" : "failing";

        if (isRunning()) {
            status += " running";
        }

        return status;
    }

    @JsonProperty
    public String lastBuildName() {
        return job.getLastBuild() != null
                ? job.getLastBuild().getDisplayName()
                : null;
    }

    @JsonProperty
    public String lastBuildUrl() {
        return job.getLastBuild() != null
                ? job.getLastBuild().getUrl()
                : null;
    }

    @JsonProperty
    public String lastBuildDuration() {
        // todo: extract to lastBuild which has ::elapsedTime and ::duration methods
        if (isRunning()) {
            return formatted(lastBuildElapsedTime());
        } else if (null != job.getLastBuild()) {
            return formatted(job.getLastBuild().getDuration());
        }

        return "";
    }

    @JsonProperty
    public String estimatedDurationOfNextBuild() {
        if (null != job.getLastBuild() && job.getLastBuild().getEstimatedDuration() > 0) {
            return formatted(job.getLastBuild().getEstimatedDuration());
        }

        return "";
    }

    private String formatted(long duration) {
        return new Duration(duration).toString();
    }

    @JsonProperty
    public int progress() {
        if (! isRunning()) {
            return 0;
        }

        if (lastBuildElapsedTime() > estimatedDuration()) {
            return 100;
        }

        if (estimatedDuration() > 0) {
            return (int) ((float) lastBuildElapsedTime() / (float) estimatedDuration() * 100);
        }

        return 100;
    }

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

    private long lastBuildElapsedTime() {
        final long now = systemTime.getTime();

        return now - whenTheLastBuildStarted();
    }

    private Result lastCompletedBuildResult() {
        Run<?, ?> previousBuild = job.getLastBuild();

        while (isRunning(previousBuild)) {
            previousBuild = previousBuild.getPreviousBuild();
        }

        return previousBuild != null
                ? previousBuild.getResult()
                : NOT_BUILT;
    }

    private boolean isSuccessful() {
        return lastCompletedBuildResult() == SUCCESS;
    }

    private boolean isRunning() {
        return isRunning(job.getLastBuild());
    }

    private boolean isRunning(Run<?, ?> build) {
        return (build != null)
                && (build.hasntStartedYet() || build.isBuilding() || build.isLogUpdated());
    }

    //todo: extract the Duration class, or move it to a BuildView class
    private static class Duration {
        private final long duration;

        private final static long MILLISECOND =    1;
        private final static long SECONDS     = 1000 * MILLISECOND;
        private final static long MINUTES     =   60 * SECONDS;
        private final static long HOURS       =   60 * MINUTES;

        private Duration(long milliseconds) {
            this.duration = milliseconds;
        }

        public long hours() {
            return duration / HOURS;
        }

        public long minutes() {
            return (duration - (hours() * HOURS)) / MINUTES ;
        }

        public long seconds() {
            return (duration - (hours() * HOURS) - (minutes() * MINUTES) ) / SECONDS;
        }

        public String toString() {
            String formatted;

            formatted  = hours() > 0   ? hours() + "h " : "";
            formatted += minutes() > 0 ? minutes() + "m " : "";
            formatted += seconds() + "s";

            return formatted;
        }
    }
}