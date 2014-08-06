package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.Functions;
import hudson.model.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import java.util.*;

import static hudson.model.Result.SUCCESS;

/**
 * @author Jan Molak
 */
public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;
    private final BuildAugmentor augmentor;
    private final RelativeLocation relative;
    private final boolean displayRelativeName;

    public static JobView of(Job<?, ?> job) {
        return new JobView(job, new BuildAugmentor(), RelativeLocation.of(job), new Date(), true);
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), true);
    }
    
    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor, boolean displayRelativeName) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), displayRelativeName);
    }

    public static JobView of(Job<?, ?> job, RelativeLocation location) {
        return new JobView(job, new BuildAugmentor(), location, new Date(), true);
    }

    public static JobView of(Job<?, ?> job, Date systemTime) {
        return new JobView(job, new BuildAugmentor(), RelativeLocation.of(job), systemTime, true);
    }

    @JsonProperty
    public String name() {
        if (displayRelativeName){
            return relative.name();
        } else {
            return job.getDisplayName();
        }
    }

    @JsonProperty
    public String url() {
        return relative.url();
    }

    @JsonProperty
    public String status() {
        // todo: consider introducing a BuildResultJudge to keep this logic in one place
        String status = lastCompletedBuild().result() == SUCCESS
                ? "successful"
                : "failing";

        if (lastBuild().isRunning()) {
            status += " running";
        }

        if (lastCompletedBuild().isClaimed()) {
            status += " claimed";
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
    public boolean shouldIndicateCulprits() {
        return ! isClaimed() && culprits().size() > 0;
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
        };

        return culprits;
    }

    @JsonProperty
    public boolean isClaimed() {
        return lastCompletedBuild().isClaimed();
    }

    @JsonProperty
    public String claimAuthor() {
        return lastCompletedBuild().claimant();
    }

    @JsonProperty
    public String claimReason() {
        return lastCompletedBuild().reasonForClaim();
    }

    @JsonProperty
    public boolean hasKnownFailures() {
        return lastCompletedBuild().hasKnownFailures();
    }

    @JsonProperty
    public List<String> knownFailures() {
        return lastCompletedBuild().knownFailures();
    }

    public String toString() {
        return name();
    }


    private JobView(Job<?, ?> job, BuildAugmentor augmentor, RelativeLocation relative, Date systemTime, Boolean displayRelativeName) {
        this.job        = job;
        this.augmentor  = augmentor;
        this.systemTime = systemTime;
        this.relative   = relative;
        this.displayRelativeName = displayRelativeName;
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

        return BuildView.of(job.getLastBuild(), augmentor, systemTime);
    }
}