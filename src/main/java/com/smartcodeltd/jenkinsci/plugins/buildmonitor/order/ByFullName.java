package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;
import org.kohsuke.stapler.DataBoundConstructor;

public class ByFullName extends BaseOrder implements Comparator<Job<?, ?>>, Serializable {

    @DataBoundConstructor
    public ByFullName() {}

    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getFullName().compareToIgnoreCase(b.getFullName());
    }

    @Extension
    public static class ByFullNameDescriptor extends Descriptor<BaseOrder> {}
}
