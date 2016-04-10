package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import hudson.model.Job;

import java.util.Comparator;

/**
 * Inspired in <a href="https://github.com/Mercynary">@Mercenary</a>'s answer to
 * issue <a href="https://github.com/jan-molak/jenkins-build-monitor-plugin/issues/113">#113</a>.
 */
public class ByLastBuild implements Comparator<Job<?, ?>> {

    @Override
    public int compare(Job<?, ?> a, Job<?, ?> b) {

    	// none of then executed
    	if (a.getLastBuild() == null && b.getLastBuild() == null) {
    		return 0;
    	}

    	// just job B executed, so job A it's older than job B
    	if (a.getLastBuild() == null && b.getLastBuild() != null) {
    		return 1;
    	}

    	// just job A executed, so job A it's newer than Job B
    	if (a.getLastBuild() != null && b.getLastBuild() == null) {
    		return -1;
    	}

    	// ordering from newer to older
        return (a.getLastBuild().getTimestamp().compareTo(b.getLastBuild().getTimestamp()) * -1);
    }
}