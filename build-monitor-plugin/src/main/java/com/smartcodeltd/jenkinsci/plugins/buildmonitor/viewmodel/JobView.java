package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.Feature;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static hudson.model.Result.SUCCESS;
import static java.lang.String.format;

/**
 * @author Jan Molak
 */
@JsonSerialize(using = JobViewSerializer.class)
public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;
    private final RelativeLocation relative;

    private final List<Feature> features = newArrayList();

    public static JobView of(Job<?, ?> job, List<Feature> features) {
        return new JobView(job, features, RelativeLocation.of(job), new Date());
    }

    public JobView(Job<?, ?> job, List<Feature> features, RelativeLocation relative, Date systemTime) {
        this.job        = job;
        this.relative   = relative;
        this.systemTime = systemTime;

        for (Feature feature : features) {
            this.features.add(feature.of(this));
        }
    }

    public List<Feature> features() {
        return ImmutableList.copyOf(features);
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
                ? duration.toString()
                : "";
    }

    public int progress() {
        return lastBuild().progress();
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

    // todo track by job.hashCode messes up the animation
    public int hashCode() {
        return Objects.hashCode(job.hashCode());
    }

    public String toString() {
        return name();
    }

    // --

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

        return BuildView.of(job.getLastBuild(), relative, systemTime);
    }
}