package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jan Molak
 */
public class Loops {

    public static <T> List<T> asFollows(T... examples) {
        return Arrays.asList(examples);
    }

    public static <T> List<T> asFollows(Supplier<T>... examples) {
        return Lists.transform(Arrays.asList(examples), Suppliers.<T>supplierFunction());
    }

}