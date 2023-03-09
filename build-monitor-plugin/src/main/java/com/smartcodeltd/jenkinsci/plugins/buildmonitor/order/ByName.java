package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;

public class ByName implements Comparator<Job<?, ?>>, Serializable {
    @Override
    public int compare(Job<?, ?> a, Job<?,?> b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
