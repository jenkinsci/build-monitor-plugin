package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.util.Comparator;
import jenkins.model.Jenkins;

public abstract class BaseOrder implements Describable<BaseOrder>, Comparator<Job<?, ?>> {

    @Override
    public Descriptor<BaseOrder> getDescriptor() {
        return Jenkins.get().getDescriptor(this.getClass());
    }
}
