package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.HumanReadableDuration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Lists.reverse;
import static hudson.model.Result.SUCCESS;

/**
 * @author Jan Molak
 */
public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;
    private final BuildAugmentor augmentor;
    private final RelativeLocation relative;
    private final Config config;

    public static JobView of(Job<?, ?> job, Config config, BuildAugmentor augmentor) {
        return new JobView(job, config, augmentor, RelativeLocation.of(job), new Date());
    }

    public JobView(Job<?, ?> job, Config config, BuildAugmentor augmentor, RelativeLocation relative, Date systemTime) {
        this.job        = job;
        this.config     = config;
        this.augmentor  = augmentor;
        this.relative   = relative;
        this.systemTime = systemTime;
    }

    @JsonProperty
    public String name() {
        return relative.name();
    }

    @JsonProperty
    public String url() {
        return relative.url();
    }

    @JsonProperty
    public String status() {
        return CssStatus.of(this);
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

    @JsonProperty
    public String timeElapsedSinceLastBuild() {
        return formatted(lastCompletedBuild().timeElapsedSince());
    }

    @JsonProperty
    public String estimatedTimeLeft() {
        Duration estimatedDuration = lastCompletedBuild().estimatedDuration();
        Duration timeElapsedSince = lastCompletedBuild().timeElapsedSince();
        if (estimatedDuration == null || timeElapsedSince == null)
            return formatted(null);

        long timeLeft = estimatedDuration.toLong() - timeElapsedSince.toLong();
        return formatted(new HumanReadableDuration(timeLeft >= 0 ? timeLeft : 0));
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
    public String headline() {
        // todo: extract and clean up
        List<BuildViewModel> failedBuildsAsc = reverse(failedBuilds());

        switch(failedBuildsAsc.size()) {
            case 0:
                return "";

            case 1:
                return Lister.describe(
                    "",
                    "Failed after %s committed their changes",
                    newLinkedList(failedBuildsAsc.get(0).culprits())
                 );

            default:
                String buildsFailedSince = Pluraliser.pluralise(
                        "%s build has failed",
                        "%s builds have failed",
                        failedBuildsAsc.size() - 1
                );

                return Lister.describe(
                        buildsFailedSince,
                        buildsFailedSince + " since %s committed their changes",
                        newLinkedList(failedBuildsAsc.get(0).culprits())
                );
        }
    }

    private List<BuildViewModel> failedBuilds() {
        List<BuildViewModel> failedBuilds = Lists.newArrayList();

        BuildViewModel build = lastBuild();
        while (! SUCCESS.equals(build.result())) {

            if (! build.isRunning()) {
                failedBuilds.add(build);
            }

            if (! build.hasPreviousBuild()) {
                break;
            }

            build = build.previousBuild();
        }

        return failedBuilds;
    }

    public boolean isDisabled() {
        return ! job.isBuildable();
    }

    public boolean isRunning() {
        return lastBuild().isRunning();
    }

    public Result lastResult() {
        return lastCompletedBuild().result();
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

    @JsonProperty
    public boolean shouldVisualizeChangeLog() {
        if (job.getLastBuild() == null) // no builds whatsoever
            return false;

        if (config.getChangeSetVisualization() == Config.ChangeSetVisualizationType.Hidden)
            return false;

        if (config.getChangeSetVisualization() == Config.ChangeSetVisualizationType.NextBuildOnly && !lastBuild().isRunning())
            return false;

        return true;
    }

    @JsonProperty
    public List<String> changeLog() {
        BuildViewModel buildForChangeLogFetching = getBuildForChangeLogFetching();
        return buildForChangeLogFetching != null ? buildForChangeLogFetching.changeLog() : null;
    }

    @JsonProperty
    public boolean hasChangeLogComputed() {
        BuildViewModel buildForChangeLogFetching = getBuildForChangeLogFetching();
        return buildForChangeLogFetching != null && buildForChangeLogFetching.hasChangeLogComputed();
    }

    @JsonProperty
    public boolean isChangeLogForUpcomingBuild() {
        BuildViewModel buildForChangeLogFetching = getBuildForChangeLogFetching();
        return buildForChangeLogFetching != null && buildForChangeLogFetching.isRunning();
    }

    private BuildViewModel getBuildForChangeLogFetching() {
        switch (config.getChangeSetVisualization()) {
            case LastOrNextBuild:
            case NextBuildOnly:
                return lastBuild();
            case LastBuildOnly:
                return lastCompletedBuild();
            case Hidden:
            default:
                return null;
        }
    }

    @JsonProperty
    public boolean buildTimeCountsDown() {
        return config.getBuildTimeVisualization() == Config.BuildTimeVisualizationType.ShowRemaining;
    }

    // todo track by job.hashCode messes up the animation
    @JsonProperty @Override
    public int hashCode() {
        return Objects.hashCode(job.hashCode());
    }

    public String toString() {
        return name();
    }

    // --

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

        return BuildView.of(build, config, augmentor, relative, systemTime);
    }
}