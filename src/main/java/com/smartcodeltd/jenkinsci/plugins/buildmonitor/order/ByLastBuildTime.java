package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Inspired by <a href="https://github.com/Mercynary">@Mercenary</a>'s answer to
 * issue <a href="https://github.com/jenkinsci/build-monitor-plugin/issues/113">#113</a>.
 */
public class ByLastBuildTime extends BaseOrder implements Comparator<Job<?, ?>>, Serializable {

    @DataBoundConstructor
    public ByLastBuildTime() {}

    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {

        // none of them executed
        if (a.getLastBuild() == null && b.getLastBuild() == null) {
            return 0;
        }

        // only job B executed, so job A is older than job B
        if (a.getLastBuild() == null && b.getLastBuild() != null) {
            return 1;
        }

        // only job A executed, so job A is newer than Job B
        if (a.getLastBuild() != null && b.getLastBuild() == null) {
            return -1;
        }

        // "New is always better" - B. Stinson ;-)
        return b.getLastBuild().getTimestamp().compareTo(a.getLastBuild().getTimestamp());
    }

    @Extension
    public static class ByLastBuildTimeDescriptor extends Descriptor<BaseOrder> {}
}
