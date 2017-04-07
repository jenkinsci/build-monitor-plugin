package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.nameFilter;

/**
 * @author Vincent & Robert
 */
public class NameFilterConfig {
    public final String prefix, suffix, regex;

    public NameFilterConfig(String prefix, String suffix, String regex) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.regex = regex;
    }
}