package net.serenitybdd.integration.utils;

import edu.umd.cs.findbugs.annotations.NonNull;

public class Nulls {
    public static <T> T coalesce(T... items) {
        for(T i : items) if(i != null) return i;
        return null;
    }

    public static <T> T getOrElse(T optionalValue, @NonNull T defaultValue) {
        return optionalValue != null ? optionalValue : defaultValue;
    }
}
