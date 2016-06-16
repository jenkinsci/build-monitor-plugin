package net.serenitybdd.integration.utils;

import javax.validation.constraints.NotNull;

public class Nulls {

    private Nulls(){}

    public static <T> T coalesce(T... items) {
        for(T i : items) if(i != null) return i;
        return null;
    }

    public static <T> T getOrElse(T optionalValue, @NotNull T defaultValue) {
        return optionalValue != null ? optionalValue : defaultValue;
    }
}
