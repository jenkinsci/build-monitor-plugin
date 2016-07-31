package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Optional;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.DurationInMilliseconds;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.HumanReadableDuration;
import hudson.model.*;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.functions.NullSafety.getOrElse;

public class BuildView implements BuildViewModel {

    private final Run<?,?> build;
    private final RelativeLocation parentJobLocation;
    private final Date systemTime;

    public static BuildView of(Run<?, ?> build) {
        return new BuildView(build, RelativeLocation.of(build.getParent()), new Date());
    }

    public static BuildView of(Run<?, ?> build, Date systemTime) {
        return new BuildView(build, RelativeLocation.of(build.getParent()), systemTime);
    }

    public static BuildView of(Run<?, ?> build, RelativeLocation parentJobLocation, Date systemTime) {
        return new BuildView(build, parentJobLocation, systemTime);
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
        return (build.hasntStartedYet() || build.isBuilding() || build.isLogUpdated());
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
        if (! isRunning()) {
            return 0;
        }

        if (isTakingLongerThanUsual()) {
            return 100;
        }

        long elapsedTime       = now() - whenTheBuildStarted(),
             estimatedDuration = build.getEstimatedDuration();

        if (estimatedDuration > 0) {
            return (int) ((float) elapsedTime / (float) estimatedDuration * 100);
        }

        return 100;
    }

    @Override
    public String description() {
        return getOrElse(build.getDescription(), "");
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
        return new BuildView(build.getPreviousBuild(), this.parentJobLocation, systemTime);
    }

    @Override
    public Set<String> culprits() {
        Set<String> culprits = new TreeSet<String>();

        if (build instanceof AbstractBuild<?, ?>) {
            AbstractBuild<?, ?> jenkinsBuild = (AbstractBuild<?, ?>) build;

            if (! (isRunning(jenkinsBuild))) {
                for (User culprit : jenkinsBuild.getCulprits()) {
                    culprits.add(culprit.getFullName());
                }
            }
        }

        return culprits;
    }

    @Override
    public <A extends Action> Optional<A> detailsOf(Class<A> jenkinsAction) {
        return Optional.fromNullable(build.getAction(jenkinsAction));
    }

    public String toString() {
        return name();
    }


    private long now() {
        return systemTime.getTime();
    }

    private long whenTheBuildStarted() {
        return build.getTimestamp().getTimeInMillis();
    }


    private BuildView(Run<?, ?> build, RelativeLocation parentJobLocation, Date systemTime) {
        this.build = build;
        this.parentJobLocation = parentJobLocation;
        this.systemTime = systemTime;
    }
}