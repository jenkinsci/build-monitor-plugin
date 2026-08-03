package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;
import org.kohsuke.stapler.DataBoundConstructor;

public class ByName extends BaseOrder implements Comparator<Job<?, ?>>, Serializable {

    @DataBoundConstructor
    public ByName() {}

    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }

    @Extension
    public static class ByNameDescriptor extends Descriptor<BaseOrder> {}
}
