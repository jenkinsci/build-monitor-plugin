package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.google.common.base.Objects;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.order.ByName;
import hudson.model.Job;

import java.util.Comparator;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.functions.NullSafety.getOrElse;

public class Config {

    private boolean displayCommitters;

    public static Config defaultConfig() {
        return new Config();
    }

    public Comparator<Job<?, ?>> getOrder() {
        /*
         * Jenkins unmarshals objects from config.xml by setting their private fields directly and without invoking their constructors.
         * In order to retrieve a potentially already persisted field try to first get the field, if that didn't work - use defaults.
         *
         * This is defensive coding to avoid issues such as this one:
         *  https://github.com/jan-molak/jenkins-build-monitor-plugin/issues/43
         */

        return getOrElse(order, new ByName());
    }

    public void setOrder(Comparator<Job<?, ?>> order) {
        this.order = order;
    }

    public boolean shouldDisplayCommitters() {
        return getOrElse(displayCommitters, true);
    }

    public void setDisplayCommitters(boolean flag) {
        this.displayCommitters = flag;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("order", order.getClass().getSimpleName())
                .add("branchesToInclude", branchesToInclude)
                .add("branchesToExclude", branchesToExclude)
                .toString();
    }

    // --

    private Comparator<Job<?, ?>> order;

    private String branchesToInclude;

    public String getBranchesToInclude() {
        return branchesToInclude;
    }

    public void setBranchesToInclude(String branchesToInclude) {
        this.branchesToInclude = branchesToInclude;
    }

    private String branchesToExclude;

    public String getBranchesToExclude() {
        return branchesToExclude;
    }

    public void setBranchesToExclude(String branchesToExclude) {
        this.branchesToExclude = branchesToExclude;
    }
}