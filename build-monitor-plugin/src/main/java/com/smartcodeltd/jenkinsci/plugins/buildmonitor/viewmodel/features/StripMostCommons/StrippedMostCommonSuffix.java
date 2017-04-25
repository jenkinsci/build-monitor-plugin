package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Vincent & Robert
 */
public class StrippedMostCommonSuffix {
    private final boolean value;

    public StrippedMostCommonSuffix(boolean value) {
        this.value = value;
    }

    @JsonValue
    public boolean value() {
        return value;
    }

    public StrippedMostCommonSuffix asJson() {
        return this;
    }
}