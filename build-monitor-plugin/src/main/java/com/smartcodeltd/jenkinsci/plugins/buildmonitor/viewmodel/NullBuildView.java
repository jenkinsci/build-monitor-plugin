package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Optional;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import hudson.model.Action;
import hudson.model.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NullBuildView implements BuildViewModel {

    @Override
    public String name() {
        return "";
    }

    @Override
    public String url() {
        return "";
    }

    @Override
    public Result result() {
        return Result.NOT_BUILT;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public Duration elapsedTime() {
        return null;
    }

    @Override
    public Duration timeElapsedSince() {
        return null;
    }

    @Override
    public Duration duration() {
        return null;
    }

    @Override
    public Duration estimatedDuration() {
        return null;
    }

    @Override
    public int progress() {
        return 0;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String status() {
        return "";
    }

    @Override
    public boolean hasPreviousBuild() {
        return false;
    }

    @Override
    public BuildViewModel previousBuild() {
        return null;
    }

    @Override
    public Set<String> culprits() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> committers() {
        return new HashSet<String>();
    }

    @Override
    public <A extends Action> Optional<A> detailsOf(Class<A> jenkinsAction) {
        return Optional.absent();
    }

	@Override
	public <A extends Action> List<A> allDetailsOf(Class<A> jenkinsAction) {
		return new ArrayList<A>();
	}
}
