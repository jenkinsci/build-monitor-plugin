package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;

import java.util.Comparator;

public class ByStatus implements Comparator<Job<?, ?>> {
    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return bothProjectsHaveBuildHistory(a, b) ?
            compareLastBuilds(a, b) :
            compareProjects(a, b);
    }

    // --

    private boolean bothProjectsHaveBuildHistory(Job<?, ?> a, Job<?, ?> b) {
        return a.getLastBuild() != null && b.getLastBuild() != null;
    }

    private int compareProjects(Job<?, ?> a, Job<?, ?> b) {
        Run<?, ?> lastBuildOfA = a.getLastBuild();
        Run<?, ?> lastBuildOfB = b.getLastBuild();

        if (lastBuildOfA == null && lastBuildOfB != null) {
            return -1;
        } else if (lastBuildOfA != null && lastBuildOfB == null) {
            return 1;
        } else {
            return 0;
        }
    }

    private int compareLastBuilds(Job<?, ?> a, Job<?, ?> b) {
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
