package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jan Molak
 */
public class Loops {

    public static <T> List<T> asFollows(T... examples) {
        return Arrays.asList(examples);
    }
}