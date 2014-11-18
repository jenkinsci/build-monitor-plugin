package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

import static hudson.model.Result.*;

/**
 * @author Jan Molak
 */
public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;
    private final BuildAugmentor augmentor;
    private final RelativeLocation relative;
    private final boolean showDownstreamJobs;
    private final List<JobView> downstreamJobs;
    private final Job<?, ?> upstreamJob;

    private final static Map<Result, String> statuses = new HashMap<Result, String>() {{
        put(SUCCESS,   "successful");
        put(UNSTABLE,  "unstable");
        put(FAILURE,   "failing");
        put(NOT_BUILT, "not-built");
        put(ABORTED,   "failing");  // if someone has aborted it then something is clearly not right, right? :)
    }};

    public static JobView of(Job<?, ?> job) {
        return new JobView(job, new BuildAugmentor(), RelativeLocation.of(job), new Date(), null, false);
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), null, false);
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor, Job<?, ?> upstreamJob) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), upstreamJob, false);
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor, Job<?, ?> upstreamJob, boolean showDownstreamJobs) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), upstreamJob, showDownstreamJobs);
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor, boolean showDownstreamJobs) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), null, showDownstreamJobs);
    }

    public static JobView of(Job<?, ?> job, RelativeLocation location) {
        return new JobView(job, new BuildAugmentor(), location, new Date(), null, false);
    }

    public static JobView of(Job<?, ?> job, Date systemTime) {
        return new JobView(job, new BuildAugmentor(), RelativeLocation.of(job), systemTime, null, false);
    }

    @JsonProperty
    public String name() {
        return relative.name();
    }

    @JsonProperty
    public String url() {
        return relative.url();
    }

    private String statusOf(Result result) {
        return statuses.containsKey(result) ? statuses.get(result) : "unknown";
    }

    @JsonProperty
    public String status() {
        String status = statusOf(lastCompletedBuild().result());

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

    @JsonProperty
    public boolean showDownstreamJobs() {
        return (showDownstreamJobs && (downstreamJobs.size() > 0));
    }

    @JsonProperty
    public List<JobView> downstreamJobs() {
        return downstreamJobs;
    }

    public String toString() {
        return name();
    }

    public void addDownstreamJob(JobView downstreamJob) {
        this.downstreamJobs.add(downstreamJob);
    }

    private JobView(Job<?, ?> job, BuildAugmentor augmentor, RelativeLocation relative, Date systemTime, Job<?, ?> upstreamJob, boolean showDownstreamJobs) {
        this.job        = job;
        this.augmentor  = augmentor;
        this.systemTime = systemTime;
        this.relative   = relative;
        this.upstreamJob = upstreamJob;
        this.showDownstreamJobs = showDownstreamJobs;
        this.downstreamJobs = new ArrayList<JobView>();
    }

    private BuildViewModel lastBuild() {
        // If this has upstreamJob, it's presumed user wants build attached by project, not last overall
        if (upstreamJob != null) {

            AbstractBuild<?, ?> upstreamBuild = (AbstractBuild) upstreamJob.getLastBuild();
            if (upstreamJob.getLastBuild() != null) {

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
            return buildViewOf(null);
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
        if (null == build) {
            return new NullBuildView();
        }

        return BuildView.of(build, augmentor, relative, systemTime);
    }
}
