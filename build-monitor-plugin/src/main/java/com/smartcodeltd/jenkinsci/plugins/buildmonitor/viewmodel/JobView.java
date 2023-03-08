package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.Feature;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.util.RunList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * @author Jan Molak
 */
@JsonSerialize(using = JobViewSerialiser.class)
public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;
    private final boolean isPipelineJob;
    private final RelativeLocation relative;

    private final List<Feature> features = new ArrayList<>();

    public static JobView of(Job<?, ?> job, List<Feature> features, boolean isPipelineJob) {
        return new JobView(job, features, isPipelineJob, RelativeLocation.of(job), new Date());
    }

    public JobView(Job<?, ?> job, List<Feature> features, boolean isPipelineJob, RelativeLocation relative, Date systemTime) {
        this.job           = job;
        this.isPipelineJob = isPipelineJob;
        this.relative      = relative;
        this.systemTime    = systemTime;

        for (Feature feature : features) {
            this.features.add(feature.of(this));
        }
    }

    public List<Feature> features() {
        return Collections.unmodifiableList(new ArrayList<>(features));
    }

    public <F extends Feature> F which(Class<F> requestedFeature) {
        for (Feature feature : features) {
            if (feature.getClass() == requestedFeature) {
                return (F) feature;
            }
        }

        throw new RuntimeException(format("%s is not a feature of this project: '%s'", requestedFeature.getSimpleName(), job.getName()));
    }

    public String name() {
        return relative.name();
    }

    public String url() {
        return relative.url();
    }

    public String status() {
        return CssStatus.of(this);
    }

    public String estimatedDuration() {
        return formatted(lastBuild().estimatedDuration());
    }

    public String timeElapsedSinceLastBuild() {
        return formatted(lastCompletedBuild().timeElapsedSince());
    }

    private String formatted(Duration duration) {
        return null != duration
                ? duration.value()
                : "";
    }

    public int progress() {
        return lastBuild().progress();
    }

    public boolean isDisabled() {
        return ! (isJobBuildable() || isJobExternal());
    }

    private boolean isJobBuildable() {
        return job.isBuildable();
    }

    private boolean isJobExternal() {
        return job instanceof hudson.model.ViewJob;
    }

    public boolean isRunning() {
        return lastBuild().isRunning();
    }

    public Result lastResult() {
        return lastCompletedBuild().result();
    }

    // todo track by job.hashCode messes up the animation
    public int hashCode() {
        return Objects.hashCode(job.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(job, obj);
    }

    @Override
    public String toString() {
        return name();
    }

    // --

    @SuppressWarnings("unchecked")
	public List<BuildViewModel> currentBuilds() {
    	List<BuildViewModel> currentBuilds = new ArrayList<>();
    	
    	RunList<Run<?, ?>> runList = ((RunList<Run<?, ?>>)job.getNewBuilds()).filter(BuildingPredicate.INSTANCE);

    	for (Run<?, ?> run : runList) {
    		currentBuilds.add(buildViewOf(run));
    	}
    	
        return currentBuilds;
    }

    public BuildViewModel lastBuild() {
        return buildViewOf(job.getLastBuild());
    }

    public BuildViewModel lastCompletedBuild() {
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

        return BuildView.of(build, isPipelineJob, relative, systemTime);
    }
}