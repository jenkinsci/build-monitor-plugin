package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractProject;

import java.util.Comparator;

public class ByName implements Comparator<AbstractProject> {
    @Override
    public int compare(AbstractProject a, AbstractProject b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
