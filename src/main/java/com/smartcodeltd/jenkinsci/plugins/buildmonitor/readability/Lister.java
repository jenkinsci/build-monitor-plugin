package com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability;

import java.util.List;

import static java.lang.String.format;

public class Lister {
    private static final String DEFAULT_NO_ITEMS_TEMPLATE = "%s";

    public static <T> String describe(String pluralTemplate, List<T> items) {
        return describe(DEFAULT_NO_ITEMS_TEMPLATE, pluralTemplate, items);
    }

    public static <T> String describe(String noItemsTemplate, String pluralTemplate, List<T> items) {
        return describe(noItemsTemplate, pluralTemplate, pluralTemplate, items);
    }

    public static <T> String describe(String noItemsTemplate, String singularTemplate, String pluralTemplate, List<T> items) {
        switch (items.size()) {
            case 0:  return formatted(noItemsTemplate,  items);
            case 1:  return formatted(singularTemplate, items);
            default: return formatted(pluralTemplate,   items);
        }
    }

    public static <T> String asString(List<T> items) {
        return items.isEmpty() ? "" : asString(headOf(items), restOf(items));
    }

    // --

    private static <T> String formatted(String template, List<T> items) {
        return format(template, asString(items));
    }

    private static <T> String asString(String acc, List<T> tail) {
        switch(tail.size()) {
            case 0:  return acc;
            case 1:  return and(acc, headOf(tail));
            default: return asString(comma(acc, headOf(tail)), restOf(tail));
        }
    }

    private static String and(String first, String second) {
        return format("%s and %s", first, second);
    }

    private static String comma(String first, String second) {
        return format("%s, %s", first, second);
    }

    private static <T> String headOf(List<T> items) {
        return items.get(0).toString();
    }

    private static <T> List<T> restOf(List<T> items) {
        return items.subList(1, items.size());
    }
}
