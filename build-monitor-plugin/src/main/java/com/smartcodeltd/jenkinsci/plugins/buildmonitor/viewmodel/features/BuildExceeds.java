package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Vincent & Robert
 */
public class BuildExceeds {
    private final boolean value;

    public BuildExceeds(boolean value) {
        this.value = value;
    }

    @JsonValue
    public boolean value() {
        return value;
    }

    public BuildExceeds asJson() {
        return this;
    }
}
