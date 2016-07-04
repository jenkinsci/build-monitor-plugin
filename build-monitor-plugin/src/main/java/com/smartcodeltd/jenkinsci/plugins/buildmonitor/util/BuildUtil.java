package com.smartcodeltd.jenkinsci.plugins.buildmonitor.util;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;

import java.util.List;


public class BuildUtil {

    public static int pipelineBuildNumber(AbstractBuild<?, ?> currentBuild) {
        int buildNumber;

        AbstractProject<?, ?> currentProject = currentBuild.getProject();
        List<AbstractProject> upstreamProjects = currentProject.getUpstreamProjects();
        if (upstreamProjects.isEmpty()) {
            buildNumber = currentBuild.getNumber();
        } else  {
            buildNumber = pipelineBuildNumber(getUpstreamBuild(upstreamProjects.get(0), currentBuild));
        }
        return buildNumber;
    }

    //Modified version of getDownStreamBuild from class BuildUtil in plugin Build pipeline plugin
    private static AbstractBuild<?, ?> getUpstreamBuild(final AbstractProject<?, ?> upstreamProject, final AbstractBuild<?, ?> currentBuild) {
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
