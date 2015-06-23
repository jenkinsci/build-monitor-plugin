package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.Augmentation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.AbstractBuild;
import hudson.model.Job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Sugar {

    public static JobStateRecipe job() {
        return new JobStateRecipe();
    }

    public static Job<?, ?> a(JobStateRecipe recipe) {
        return recipe.execute();
    }

    public static BuildStateRecipe build() {
        return new BuildStateRecipe();
    }

    public static AbstractBuild a(BuildStateRecipe recipe) {
        return recipe.execute();
    }

    public static RelativeLocation locatedAt(String url) {

        RelativeLocation location = mock(RelativeLocation.class);
        when(location.url()).thenReturn(url);

        return location;
    }

    public static BuildAugmentor augmentedWith(Class<? extends Augmentation>... augmentationsToSupport) {
        BuildAugmentor augmentor = new BuildAugmentor();

        for (Class<? extends Augmentation> augmentation : augmentationsToSupport) {
            augmentor.support(augmentation);
        }

        return augmentor;
    }

    public static Config withDefaultConfig() {
        return new ConfigStateRecipe().execute();
    }

    public static Config configuredTo(ConfigStateRecipe recipe) {
        return recipe.execute();
    }
}
