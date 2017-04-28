package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.jobNameFilter;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Vincent & Robert
 */
public class JobNameFiltered {
    private final String value;

    public JobNameFiltered(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public JobNameFiltered asJson() {
        return this;
    }
}