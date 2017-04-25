package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Vincent & Robert
 */
public class StrippedMostCommonPrefix {
    private final boolean value;

    public StrippedMostCommonPrefix(boolean value) {
        this.value = value;
    }

    @JsonValue
    public boolean value() {
        return value;
    }

    public StrippedMostCommonPrefix asJson() {
        return this;
    }
}