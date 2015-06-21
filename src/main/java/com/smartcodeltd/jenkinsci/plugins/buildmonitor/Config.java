package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.google.common.base.Objects;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.order.ByName;
import hudson.model.AbstractProject;

import java.util.Comparator;

public class Config {

    private Comparator<AbstractProject> order;

    public Comparator<AbstractProject> getOrder() {
        return order;
    }

    public void setOrder(Comparator<AbstractProject> order) {
        this.order = order;
    }

    public static Config defaultConfig() {
        // To avoid issues when Jenkins instantiates the plugin without populating its fields
        // Ensure there is a dafult for every value
        // https://github.com/jan-molak/jenkins-build-monitor-plugin/issues/43
        Config config = new Config();

        config.order = new ByName();

        return config;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("order", order.getClass().getSimpleName())
                .toString();
    }

}
