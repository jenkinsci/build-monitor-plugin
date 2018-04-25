package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Predicate;
import hudson.model.Run;

import javax.annotation.Nullable;

public final class BuildingPredicate implements Predicate<Run<?, ?>> {

	public static final BuildingPredicate INSTANCE = new BuildingPredicate();
	
	private BuildingPredicate() {
	}
	
	@Override
	public boolean apply(@Nullable Run<?, ?> run) {
		if (run == null) {
			throw new RuntimeException("Run was null");
		}
		return run.isBuilding();
	}

}
