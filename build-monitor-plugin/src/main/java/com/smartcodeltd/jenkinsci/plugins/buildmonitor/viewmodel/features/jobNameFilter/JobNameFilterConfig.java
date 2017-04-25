package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.jobNameFilter;

/**
 * @author Vincent & Robert
 */
public class JobNameFilterConfig {
    public final String regex;

    public JobNameFilterConfig( String regex) {
        this.regex = regex;
    }
}