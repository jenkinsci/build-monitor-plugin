package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.base.Predicate;

import hudson.model.Run;

public final class BuildingPredicate implements Predicate<Run<?, ?>> {

	public static final BuildingPredicate INSTANCE = new BuildingPredicate();
	
	private BuildingPredicate() {
	}
	
	@Override
	public boolean apply(Run<?, ?> run) {
		return run.isBuilding();
	}

}
