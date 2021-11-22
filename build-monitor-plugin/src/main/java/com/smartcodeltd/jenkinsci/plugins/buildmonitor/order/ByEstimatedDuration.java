package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractProject;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class ByEstimatedDuration implements Comparator<AbstractProject<?, ?>>, Serializable {

    @Override
    public int compare(AbstractProject<?, ?> a, AbstractProject<?,?> b) {
        return compareEstimatedDuration(a, b);
    }

    /**
     * Returns a sum of the estimated duration for a project and all upstream projects
     *
     * @return time
     */
    private long getTotalEstimatedDuration(AbstractProject<?, ?> project) {
        long time = project.getEstimatedDuration();

        if(!project.getUpstreamProjects().isEmpty()) {
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
}
