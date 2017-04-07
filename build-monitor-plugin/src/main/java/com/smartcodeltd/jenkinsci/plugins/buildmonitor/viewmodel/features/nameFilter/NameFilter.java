package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.nameFilter;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Vincent & Robert
 */
public class NameFilter {
    private final String value;

    public NameFilter(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public NameFilter asJson() {
        return this;
    }
}