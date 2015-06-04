package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Result;
import hudson.model.AbstractProject;

import java.util.Comparator;

public class ByStatus implements Comparator<AbstractProject> {
    @Override
    public int compare(AbstractProject a, AbstractProject b) {
        Result aResult = a.getLastBuild().getResult();
        Result bResult = b.getLastBuild().getResult();
        if (aResult.isWorseThan(bResult)) {
            return -1;
        } else if (aResult.isBetterThan(bResult)) {
            return 1;
        } else {
            return 0;
        }
    }
}
