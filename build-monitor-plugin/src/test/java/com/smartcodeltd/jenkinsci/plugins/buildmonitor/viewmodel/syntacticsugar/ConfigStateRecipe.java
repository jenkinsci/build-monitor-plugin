package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.build.GetLastCompletedBuild;
import hudson.model.Job;
import java.util.Comparator;
import java.util.function.Supplier;

public class ConfigStateRecipe implements Supplier<Config> {

    private Config config = Config.defaultConfig();

    public ConfigStateRecipe order(Comparator<Job<?, ?>> order) {
        config.setOrder(order);

        return this;
    }

    public ConfigStateRecipe withBadgesFromLastCompletedBuild() {
        config.setDisplayBadgesFrom(new GetLastCompletedBuild());

        return this;
    }

    @Override
    public Config get() {
        return config;
    }
}
