package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;

import java.io.Serializable;
import java.util.Comparator;

public class ByStatus implements Comparator<Job<?, ?>>, Serializable {
    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return bothProjectsHaveBuildHistory(a, b)
            ? compareRecentlyCompletedBuilds(a, b)
            : compareProjects(a, b);
    }

    // --

    private boolean bothProjectsHaveBuildHistory(Job<?, ?> a, Job<?, ?> b) {
        return a.getLastCompletedBuild() != null && b.getLastCompletedBuild() != null;
    }

    private int compareProjects(Job<?, ?> a, Job<?, ?> b) {
        Run<?, ?> recentBuildOfA = a.getLastCompletedBuild();
        Run<?, ?> recentBuildOfB = b.getLastCompletedBuild();

        if (recentBuildOfA == null && recentBuildOfB != null) {
            return -1;
        } else if (recentBuildOfA != null && recentBuildOfB == null) {
            return 1;
        } else {
            return 0;
        }
    }

    private int compareRecentlyCompletedBuilds(Job<?, ?> a, Job<?, ?> b) {
        Result lastResultOfA = a.getLastCompletedBuild().getResult();
        Result lastResultOfB = b.getLastCompletedBuild().getResult();

        if (lastResultOfA == null && lastResultOfB == null) {
            return 0;
        } else if (lastResultOfA == null) {
            return -1;
        } else if (lastResultOfB == null) {
            return 1;
        } else if (lastResultOfA.isWorseThan(lastResultOfB)) {
            return -1;
        } else if (lastResultOfA.isBetterThan(lastResultOfB)) {
            return 1;
        } else {
            return 0;
        }
    }
}