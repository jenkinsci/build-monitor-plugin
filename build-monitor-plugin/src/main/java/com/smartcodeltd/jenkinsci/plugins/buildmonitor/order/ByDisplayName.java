package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;

public class ByDisplayName implements Comparator<Job<?, ?>>, Serializable {
    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getDisplayName().compareToIgnoreCase(b.getDisplayName());
    }
}
