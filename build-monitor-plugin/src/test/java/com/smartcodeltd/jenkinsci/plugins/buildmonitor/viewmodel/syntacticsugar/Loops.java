package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Jan Molak
 */
public class Loops {

    public static <T> List<T> asFollows(T... examples) {
        return Arrays.asList(examples);
    }

    public static <T> List<T> asFollows(Supplier<T>... examples) {
        return Arrays.stream(examples).map(Supplier::get).collect(Collectors.toList());
    }

}