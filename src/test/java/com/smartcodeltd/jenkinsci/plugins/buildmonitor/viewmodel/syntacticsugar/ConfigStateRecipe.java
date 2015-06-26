package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config;
import hudson.model.AbstractProject;

import java.util.Comparator;

public class ConfigStateRecipe implements Supplier<Config> {

    private Config config = Config.defaultConfig();

    public ConfigStateRecipe order(Comparator<AbstractProject> order) {
        config.setOrder(order);
        return this;
    }

    @Override
    public Config get() {
        return config;
    }
}
