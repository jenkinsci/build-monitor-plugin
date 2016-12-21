package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;

import hudson.model.BuildBadgeAction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Sugar {

	private static StaticJenkinsAPIs jenkins = mock(StaticJenkinsAPIs.class);
	
    public static JobViewRecipe jobView() {
        return new JobViewRecipe();
    }

    public static JobStateRecipe job() {
        return new JobStateRecipe();
    }

    public static BuildStateRecipe build() {
        return new BuildStateRecipe();
    }

    public static StaticJenkinsAPIs jenkins() {
        return jenkins;
    }

    public static BadgeRecipe badge() {
        return new BadgeRecipe(jenkins);
    }

    public static <X> X a(Supplier<X> recipe) {
        return recipe.get();
    }

    public static <X> X with(Supplier<X> recipe) {
        return recipe.get();
    }

    // Recipes needed as these get more complex..

    public static RelativeLocation locatedAt(String url) {

        RelativeLocation location = mock(RelativeLocation.class);
        when(location.url()).thenReturn(url);

        return location;
    }

    public static Config withDefaultConfig() {
        return new ConfigStateRecipe().get();
    }
}
