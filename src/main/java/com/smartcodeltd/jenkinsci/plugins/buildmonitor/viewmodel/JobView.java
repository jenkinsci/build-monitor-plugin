package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.Hudson;
import hudson.model.AbstractProject;
import hudson.model.DependencyGraph;
import com.tikal.jenkins.plugins.multijob.MultiJobProject;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public int phase() {
        return lastCompletedBuild().phase();
    }

    @JsonProperty
    public int jobsinphase() {
        return lastCompletedBuild().jobsinphase();
    }

    @JsonProperty
    public int numphases() {
        return lastCompletedBuild().numphases();
    }

    public String toString() {
        return name();
    }

    // --

    private BuildViewModel lastBuild() {
        // If we are used inside a multijob, we see the build attached to it.
        DependencyGraph depsgraph = Hudson.getInstance().getDependencyGraph();
        AbstractProject<?, ?> parent = null;
        for (final AbstractProject<?, ?> p : depsgraph.getUpstream((AbstractProject<?, ?>) job)) {
            if (p instanceof MultiJobProject) {
                parent = p;
                break;
            }
        }
        if (parent != null) {
            AbstractBuild<?, ?> upstreamBuild = (AbstractBuild) parent.getLastBuild();

            if (parent.getLastBuild() != null) {

                @SuppressWarnings("unchecked")
                final List<AbstractBuild<?, ?>> thisBuilds = (List<AbstractBuild<?, ?>>) job.getBuilds();

                for (final AbstractBuild<?, ?> innerBuild : thisBuilds) {
                    for (final CauseAction action : innerBuild.getActions(CauseAction.class)) {
                        for (final Cause cause : action.getCauses()) {
                            if (cause instanceof Cause.UpstreamCause) {
                                final Cause.UpstreamCause upstreamCause = (Cause.UpstreamCause) cause;
                                if (upstreamCause.getUpstreamProject().equals(upstreamBuild.getProject().getFullName())
                                        && (upstreamCause.getUpstreamBuild() == upstreamBuild.getNumber())) {
                                    return buildViewOf(innerBuild);
                                }
                            }
                        }
                    }
                }
            }
            return buildViewOf(job.getLastBuild(), false);
        }

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
        return buildViewOf(build, true);
    }

    private BuildViewModel buildViewOf(Run<?, ?> build, boolean current) {
        if (null == build) {
            return new NullBuildView();
        }

        return BuildView.of(build, config, augmentor, relative, systemTime, current);
    }
}
