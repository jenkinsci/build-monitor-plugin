package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasBadgesBadgePlugin;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasJunitRealtime;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Sugar {

    public static JobViewRecipe jobView() {
        return new JobViewRecipe();
    }

    public static JobStateRecipe job() {
        return new JobStateRecipe();
    }

    public static BuildStateRecipe build() {
        return new BuildStateRecipe();
    }

    public static HasBadgesBadgePlugin hasBadgePluginBadges(Config config) {
        return new HasBadgesBadgePlugin(config);
    }

    public static HasBadgesBadgePlugin hasBadgePluginBadges(ConfigStateRecipe config) {
        return new HasBadgesBadgePlugin(config.get());
    }

    public static BadgeBadgePluginRecipe badgePluginBadge() {
        return new BadgeBadgePluginRecipe();
    }

    public static HasJunitRealtime hasJunitRealtime() {
        return new HasJunitRealtime();
    }

    public static RealtimeTestPluginRecipe realtimeTest() {
        return new RealtimeTestPluginRecipe();
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

    public static ConfigStateRecipe withConfig() {
        return new ConfigStateRecipe();
    }
}
