package com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability;

import static java.lang.String.format;

public class Pluraliser {
    private Pluraliser(){}

    public static String pluralise(String template, int count) {
        return format(template, count);
    }

    public static String pluralise(String singularTemplate, String pluralTemplate, int count) {
        return count == 1
                ? pluralise(singularTemplate, count)
                : pluralise(pluralTemplate, count);
    }

    public static String pluralise(String zeroCountTemplate, String singularTemplate, String pluralTemplate, int count) {
        return count == 0
                ? pluralise(zeroCountTemplate, count)
                : pluralise(singularTemplate, pluralTemplate, count);
    }
}
