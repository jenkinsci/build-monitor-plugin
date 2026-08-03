package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;
import org.kohsuke.stapler.DataBoundConstructor;

public class ByDisplayName extends BaseOrder implements Comparator<Job<?, ?>>, Serializable {

    @DataBoundConstructor
    public ByDisplayName() {}

    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getDisplayName().compareToIgnoreCase(b.getDisplayName());
    }

    @Extension
    public static class ByDisplayNameDescriptor extends Descriptor<BaseOrder> {}
}
