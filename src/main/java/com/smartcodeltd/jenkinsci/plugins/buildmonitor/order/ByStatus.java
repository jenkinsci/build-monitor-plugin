package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;

import java.util.Comparator;

public class ByStatus implements Comparator<AbstractProject> {
    @Override
    public int compare(AbstractProject a, AbstractProject b) {
        return bothProjectsHaveBuildHistory(a, b) ?
            compareLastBuilds(a, b) :
            compareProjects(a, b);
    }

    // --

    private boolean bothProjectsHaveBuildHistory(AbstractProject a, AbstractProject b) {
        return a.getLastBuild() != null && b.getLastBuild() != null;
    }

    private int compareProjects(AbstractProject a, AbstractProject b) {
        AbstractBuild lastBuildOfA = a.getLastBuild();
        AbstractBuild lastBuildOfB = b.getLastBuild();

        if (lastBuildOfA == null && lastBuildOfB != null) {
            return -1;
        } else if (lastBuildOfA != null && lastBuildOfB == null) {
            return 1;
        } else {
            return 0;
        }
    }

    private int compareLastBuilds(AbstractProject a, AbstractProject b) {
        Result lastResultOfA = a.getLastBuild().getResult();
        Result lastResultOfB = b.getLastBuild().getResult();

        if (lastResultOfA.isWorseThan(lastResultOfB)) {
            return -1;
        } else if (lastResultOfA.isBetterThan(lastResultOfB)) {
            return 1;
        } else {
            return 0;
        }
    }
}
