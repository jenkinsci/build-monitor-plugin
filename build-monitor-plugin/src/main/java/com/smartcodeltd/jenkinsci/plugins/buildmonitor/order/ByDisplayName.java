package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractProject;
import hudson.model.Job;

import java.util.Comparator;

public class ByDisplayName implements Comparator<Job<?, ?>> {
    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getDisplayName().compareToIgnoreCase(b.getDisplayName());
    }
}
