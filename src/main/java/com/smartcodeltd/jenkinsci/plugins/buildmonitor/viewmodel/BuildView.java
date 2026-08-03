package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits.BuildCulpritsRetriever;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline.PipelineHelper;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.DurationInMilliseconds;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.HumanReadableDuration;
import hudson.model.Action;
import hudson.model.Result;
import hudson.model.Run;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BuildView implements BuildViewModel {

    private final Run<?, ?> build;
    private final boolean isPipeline;
    private final RelativeLocation parentJobLocation;
    private final Date systemTime;
    private final BuildCulpritsRetriever buildCulpritsRetriever;

    // Visible for testing
    static BuildView of(Run<?, ?> build) {
        return new BuildView(build, false, RelativeLocation.of(build.getParent()), new Date());
    }

    public static BuildView of(
            Run<?, ?> build, boolean isPipeline, RelativeLocation parentJobLocation, Date systemTime) {
        return new BuildView(build, isPipeline, parentJobLocation, systemTime);
    }

    @Override
    public String name() {
        return build.getDisplayName();
    }

    // todo: fix the double slash added when there's no parent
    @Override
    public String url() {
        return parentJobLocation.url() + "/" + build.getNumber() + "/";
    }

    @Override
    public Result result() {
        return build.getResult();
    }

    @Override
    public boolean isRunning() {
        return isRunning(this.build);
    }

    private boolean isRunning(Run<?, ?> build) {
        return build.hasntStartedYet() || build.isBuilding() || build.isLogUpdated();
    }

    @Override
    public Duration elapsedTime() {
        return new HumanReadableDuration(now() - whenTheBuildStarted());
    }

    @Override
    public Duration timeElapsedSince() {
        return new DurationInMilliseconds(now() - (whenTheBuildStarted() + build.getDuration()));
    }

    @Override
    public Duration duration() {
        return new HumanReadableDuration(build.getDuration());
    }

    @Override
    public Duration estimatedDuration() {
        return new HumanReadableDuration(build.getEstimatedDuration());
    }

    @Override
    public int progress() {
        if (!isRunning()) {
            return 0;
        }

        if (isTakingLongerThanUsual()) {
            return 100;
        }

        long elapsedTime = now() - whenTheBuildStarted(), estimatedDuration = build.getEstimatedDuration();

        if (estimatedDuration > 0) {
            return (int) ((float) elapsedTime / (float) estimatedDuration * 100);
        }

        return 100;
    }

    @Override
    public String description() {
        return Optional.ofNullable(build.getDescription()).orElse("");
    }

    @Override
    public boolean isPipeline() {
        return isPipeline;
    }

    @Override
    public List<String> pipelineStages() {
        if (PipelineHelper.isWorkflowRun(build, new StaticJenkinsAPIs())) {
            return PipelineHelper.getPipelines(build);
        }
        return List.of();
    }

    private boolean isTakingLongerThanUsual() {
        return elapsedTime().greaterThan(estimatedDuration());
    }

    @Override
    public boolean hasPreviousBuild() {
        return null != build.getPreviousBuild();
    }

    @Override
    public BuildViewModel previousBuild() {
        return new BuildView(build.getPreviousBuild(), isPipeline, this.parentJobLocation, systemTime);
    }

    @Override
    public Set<String> culprits() {
        return buildCulpritsRetriever.getCulprits(build);
    }

    @Override
    public Set<String> committers() {
        return buildCulpritsRetriever.getCommitters(build);
    }

    @Override
    public <A extends Action> Optional<A> detailsOf(Class<A> jenkinsAction) {
        return Optional.ofNullable(build.getAction(jenkinsAction));
    }

    @Override
    public <A extends Action> List<A> allDetailsOf(Class<A> jenkinsAction) {
        return build.getActions(jenkinsAction);
    }

    @Override
    public String toString() {
        return name();
    }

    private long now() {
        return systemTime.getTime();
    }

    private long whenTheBuildStarted() {
        return build.getTimestamp().getTimeInMillis();
    }

    private BuildView(Run<?, ?> build, boolean isPipeline, RelativeLocation parentJobLocation, Date systemTime) {
        this.build = build;
        this.isPipeline = isPipeline;
        this.parentJobLocation = parentJobLocation;
        this.systemTime = systemTime;
        this.buildCulpritsRetriever = BuildCulpritsRetriever.getInstanceForRun(build, new StaticJenkinsAPIs());
    }
}
