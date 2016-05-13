package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractProject;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class ByEstimatedDuration implements Comparator<AbstractProject<?, ?>> {

    private static final Logger LOGGER = Logger.getLogger(ByEstimatedDuration.class.getName());

    @Override
    public int compare(AbstractProject<?, ?> a, AbstractProject<?,?> b) {
        return compareEstimatedDuration(a, b);
    }

    /**
     * Returns a sum of the estimated duration for a project and all upstream projects
     *
     * @param project
     * @return
     */
    private long getTotalEstimatedDuration(AbstractProject<?, ?> project) {
        long time = project.getEstimatedDuration();

        if(!project.getUpstreamProjects().isEmpty()) {
            List<AbstractProject> upStreamProjects = project.getUpstreamProjects();
            for (int i = 0; i < upStreamProjects.size(); i++) {
                time += getTotalEstimatedDuration((AbstractProject<?, ?>)upStreamProjects.get(i));
            }
        }
        LOGGER.info(project.getName() + " : " + time);
        return time;
    }

    private int compareEstimatedDuration(AbstractProject<?, ?> a, AbstractProject<?, ?> b) {
        if(getTotalEstimatedDuration(a) < getTotalEstimatedDuration(b)) {
            return -1;
        } else if (getTotalEstimatedDuration(a) > getTotalEstimatedDuration(b)) {
            return 1;
        } else {
            return 0;
        }

    }
}