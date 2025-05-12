package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

public class ByEstimatedDuration extends BaseOrder implements Comparator<Job<?, ?>>, Serializable {

    @DataBoundConstructor
    public ByEstimatedDuration() {}

    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return compareEstimatedDuration((AbstractProject<?, ?>) a, (AbstractProject<?, ?>) b);
    }

    /**
     * Returns a sum of the estimated duration for a project and all upstream projects
     *
     * @return time
     */
    private long getTotalEstimatedDuration(AbstractProject<?, ?> project) {
        long time = project.getEstimatedDuration();

        if (!project.getUpstreamProjects().isEmpty()) {
            List<AbstractProject> upStreamProjects = project.getUpstreamProjects();
            for (AbstractProject upStreamProject : upStreamProjects) {
                time += getTotalEstimatedDuration((AbstractProject<?, ?>) upStreamProject);
            }
        }
        return time;
    }

    private int compareEstimatedDuration(AbstractProject<?, ?> a, AbstractProject<?, ?> b) {
        return Long.compare(getTotalEstimatedDuration(a), getTotalEstimatedDuration(b));
    }

    @Extension
    public static class ByEstimatedDurationDescriptor extends Descriptor<BaseOrder> {}
}
