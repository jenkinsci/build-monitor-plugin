package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Jan Molak
 */
public class Headline {
    private final String value;

    public Headline(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
