package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.AbstractProject;
import hudson.model.Job;

import java.util.Comparator;

public class ByFullName implements Comparator<Job<?, ?>> {
    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getFullName().compareToIgnoreCase(b.getFullName());
    }
}
