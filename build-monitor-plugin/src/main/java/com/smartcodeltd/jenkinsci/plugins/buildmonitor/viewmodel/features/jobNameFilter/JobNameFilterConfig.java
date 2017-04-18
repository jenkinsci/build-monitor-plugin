package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.jobNameFilter;

/**
 * @author Vincent & Robert
 */
public class JobNameFilterConfig {
    public final String prefix, suffix, regex;

    public JobNameFilterConfig(String prefix, String suffix, String regex) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.regex = regex;
    }
}