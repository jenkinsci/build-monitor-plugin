package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jan Molak
 */
public class SyntacticSugar {

    public static <T> List<T> asFollows(T... examples) {
        return Arrays.asList(examples);
    }
}