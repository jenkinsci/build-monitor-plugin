package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BuildView implements BuildViewModel {

    private final Run<?,?> build;
    private final Date systemTime;
    private final BuildAugmentor augmentor;

    public static BuildView of(Run<?, ?> build) {
        return new BuildView(build, new BuildAugmentor(), new Date());
    }

    public static BuildView of(Run<?, ?> build, BuildAugmentor augmentor, Date systemTime) {
        return new BuildView(build, augmentor, systemTime);
    }


    @Override
    public String name() {
        return build.getDisplayName();
    }

    @Override
    public String url() {
        return build.getUrl();
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
        return new Duration(now() - whenTheBuildStarted());
    }

    @Override
    public Duration duration() {
        return new Duration(build.getDuration());
    }

    @Override
    public Duration estimatedDuration() {
        return new Duration(build.getEstimatedDuration());
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

    private boolean isTakingLongerThanUsual() {
        return elapsedTime().greaterThan(estimatedDuration());
    }

    @Override
    public boolean hasPreviousBuild() {
        return null != build.getPreviousBuild();
    }

    @Override
    public BuildViewModel previousBuild() {
        return new BuildView(build.getPreviousBuild(), augmentor, systemTime);
    }

    @Override
    public Set<String> culprits() {
        Set<String> culprits = new HashSet<String>();

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
    public boolean isClaimed() {
        return claim().wasMade();
    }

    @Override
    public String claimant() {
        return claim().author();
    }

    @Override
    public String reasonForClaim() {
        return claim().reason();
    }

    private Claim claim() {
        return augmentor.detailsOf(build, Claim.class);
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


    private BuildView(Run<?, ?> build, BuildAugmentor augmentor, Date systemTime) {
        this.build = build;
        this.systemTime = systemTime;
        this.augmentor = augmentor;
    }
}
