package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class pipelineGroup implements Comparator<AbstractProject<?, ?>> {
    private static final Logger LOGGER = Logger.getLogger(pipelineGroup.class.getName());

    @Override
    public int compare(AbstractProject<?, ?> a, AbstractProject<?, ?> b) {
        if (pipelineBuildNumber(a) < pipelineBuildNumber(b)) {
            return -1;
        } else if (pipelineBuildNumber(a) > pipelineBuildNumber(b)) {
            return 1;
        } else {
            return 0;
        }
    }

    private int pipelineBuildNumber(AbstractProject<?, ?> project) {
        int buildNumber;

        AbstractBuild<?, ?> currentBuild = project.getLastBuild();
        List<AbstractProject> upstreamProjects = project.getUpstreamProjects();
        if (upstreamProjects.isEmpty()) {
            buildNumber = currentBuild.getNumber();
        } else if (getUpstreamBuild(upstreamProjects.get(0), currentBuild) == null) {
            buildNumber = currentBuild.getNumber();
        } else {
            buildNumber = pipelineBuildNumber(upstreamProjects.get(0));
        }
        LOGGER.info(String.valueOf(project.getName() + " : " + String.valueOf(buildNumber)));
        return buildNumber;
    }

    //Modified version of getDownStreamBuild from class BuildUtil in plugin Build pipeline plugin
    private AbstractBuild<?, ?> getUpstreamBuild(final AbstractProject<?, ?> upstreamProject, final AbstractBuild<?, ?> currentBuild) {
        if ((upstreamProject != null) && (currentBuild != null)) {
            @SuppressWarnings("unchecked")
            final List<AbstractBuild<?, ?>> upstreamBuilds = (List<AbstractBuild<?, ?>>) upstreamProject.getBuilds();
            for (final AbstractBuild<?, ?> innerBuild : upstreamBuilds) {
                for (final CauseAction action : currentBuild.getActions(CauseAction.class)) {
                    for (final Cause cause : action.getCauses()) {
                        if (cause instanceof Cause.UpstreamCause) {
                            final Cause.UpstreamCause upstreamCause = (Cause.UpstreamCause) cause;
                            if (upstreamCause.getUpstreamProject().equals(innerBuild.getProject().getFullName())
                                    && (upstreamCause.getUpstreamBuild() == innerBuild.getNumber())) {
                                return innerBuild;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}