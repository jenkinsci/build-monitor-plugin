package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Job;
import java.io.Serializable;
import java.util.Comparator;

public class ByFullName implements Comparator<Job<?, ?>>, Serializable {
    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {
        return a.getFullName().compareToIgnoreCase(b.getFullName());
    }
}
