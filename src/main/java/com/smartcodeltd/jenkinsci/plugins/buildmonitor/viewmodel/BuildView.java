package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.HumanReadableDuration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.DurationInMilliseconds;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.Analysis;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob.Multijob;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.User;

import java.util.*;

public class BuildView implements BuildViewModel {

    private final Run<?,?> build;
    private final RelativeLocation parentJobLocation;
    private final Date systemTime;
    private final BuildAugmentor augmentor;
    private final Config config;
    private final boolean current;

    public static BuildView of(Run<?, ?> build) {
        return new BuildView(build, Config.defaultConfig(), new BuildAugmentor(), RelativeLocation.of(build.getParent()), new Date(), true);
    }

    public static BuildView of(Run<?, ?> build, Config config) {
        return new BuildView(build, config, new BuildAugmentor(), RelativeLocation.of(build.getParent()), new Date(), true);
    }

    public static BuildView of(Run<?, ?> build, Config config, BuildAugmentor augmentor, Date systemTime) {
        return new BuildView(build, config, augmentor, RelativeLocation.of(build.getParent()), systemTime, true);
    }

    public static BuildView of(Run<?, ?> build, Config config, BuildAugmentor augmentor, RelativeLocation parentJobLocation, Date systemTime) {
        return new BuildView(build, config, augmentor, parentJobLocation, systemTime, true);
    }

    public static BuildView of(Run<?, ?> build, Config config, BuildAugmentor augmentor, RelativeLocation parentJobLocation, Date systemTime, boolean current) {
        return new BuildView(build, config, augmentor, parentJobLocation, systemTime, current);
    }


    @Override
    public String name() {
        return build.getDisplayName();
    }

    @Override
    public String url() {
        return parentJobLocation.url() + "/" + build.getNumber() + "/";
    }

    @Override
    public Result result() {
        if (! current) {
            return Result.NOT_BUILT;
        }
        return build.getResult();
    }

    @Override
    public boolean isRunning() {
        return isRunning(this.build);
    }

    private boolean isRunning(Run<?, ?> build) {
        if (! current) {
            return false;
        }
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

    private boolean isTakingLongerThanUsual() {
        return elapsedTime().greaterThan(estimatedDuration());
    }

    @Override
    public boolean hasPreviousBuild() {
        return null != build.getPreviousBuild();
    }

    @Override
    public BuildViewModel previousBuild() {
        return new BuildView(build.getPreviousBuild(), config, augmentor, this.parentJobLocation, systemTime, true);
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

    @Override
    public boolean hasKnownFailures() {
        return analysis().foundKnownFailures();
    }

    @Override
    public List<String> knownFailures() {
        return  analysis().failures();
    }

    private Analysis analysis() {
        return augmentor.detailsOf(build, Analysis.class);
    }

    @Override
    public int phase() {
        return multijob().phase();
    }

    @Override
    public int jobsinphase() {
        return multijob().jobsinphase();
    }

    @Override
    public int numphases() {
        return multijob().numphases();
    }

    private Multijob multijob() {
        return augmentor.detailsOf(build, Multijob.class);
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


    private BuildView(Run<?, ?> build, Config config, BuildAugmentor augmentor, RelativeLocation parentJobLocation, Date systemTime, boolean current) {
        this.build = build;
        this.config = config;
        this.augmentor = augmentor;
        this.parentJobLocation = parentJobLocation;
        this.systemTime = systemTime;
        this.current = current;
    }
}
