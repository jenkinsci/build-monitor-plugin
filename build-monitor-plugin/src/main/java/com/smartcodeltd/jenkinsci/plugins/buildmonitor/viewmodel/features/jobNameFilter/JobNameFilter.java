package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.jobNameFilter;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Vincent & Robert
 */
public class JobNameFilter {
    private final String value;

    public JobNameFilter(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public JobNameFilter asJson() {
        return this;
    }
}