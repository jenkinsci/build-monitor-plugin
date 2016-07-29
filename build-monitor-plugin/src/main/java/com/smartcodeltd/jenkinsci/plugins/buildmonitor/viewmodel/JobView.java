package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
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
    public String lastBuildDescription() {
        return lastBuild().description();
    }

    @JsonProperty
    public String estimatedDuration() {
        return formatted(lastBuild().estimatedDuration());
    }

    @JsonProperty
    public String timeElapsedSinceLastBuild() {
        return formatted(lastCompletedBuild().timeElapsedSince());
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
        BuildViewModel lastBuild = lastBuild();
        if (SUCCESS.equals(lastBuild.result())) {
            return getSuccessHeadline(lastBuild);
        } else {
            return getFailureHeadline(lastBuild);
        }
    }

    private String getSuccessHeadline(BuildViewModel lastBuild) {
        Optional<BuildViewModel> lastCompletedBuildBeforeThisOne = lastCompletedBuildBefore(lastBuild);
        if (!lastCompletedBuildBeforeThisOne.isPresent() || SUCCESS.equals(lastCompletedBuildBeforeThisOne.get().result())) {
            return "";
        } else {
            return Lister.describe(
                    "",
                    "Succeeded after %s committed their changes :-)",
                    newLinkedList(lastBuild.committers())
            );
        }
    }

    private String getFailureHeadline(BuildViewModel lastBuild) {
        List<BuildViewModel> failedBuildsNewestToOldest = failedBuildsSince(lastBuild);

        BuildViewModel firstFailedBuild = Iterables.getLast(failedBuildsNewestToOldest);

        if (failedBuildsNewestToOldest.size() == 1) {
            return Lister.describe(
                    "",
                    "Failed after %s committed their changes",
                    newLinkedList(firstFailedBuild.culprits())
            );
        } else {
            String buildsFailedSince = Pluraliser.pluralise(
                    "%s build has failed",
                    "%s builds have failed",
                    failedBuildsNewestToOldest.size() - 1
            );

            return Lister.describe(
                    buildsFailedSince,
                    buildsFailedSince + " since %s committed their changes",
                    newLinkedList(firstFailedBuild.culprits())
            );
        }
    }

    private Optional<BuildViewModel> lastCompletedBuildBefore(BuildViewModel build) {
        BuildViewModel currentBuild = build;
        do {
            if (currentBuild.hasPreviousBuild()) {
                currentBuild = currentBuild.previousBuild();
            } else {
                return Optional.absent();
            }
        } while (currentBuild.isRunning());

        return Optional.of(currentBuild);
    }

    private List<BuildViewModel> failedBuildsSince(BuildViewModel build) {
        BuildViewModel currentBuild = build;

        List<BuildViewModel> failedBuilds = Lists.newArrayList();

        while (! SUCCESS.equals(currentBuild.result())) {

            if (! currentBuild.isRunning()) {
                failedBuilds.add(currentBuild);
            }

            if (! currentBuild.hasPreviousBuild()) {
                break;
            }

            currentBuild = currentBuild.previousBuild();
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

        return BuildView.of(job.getLastBuild(), config, augmentor, relative, systemTime);
    }
}