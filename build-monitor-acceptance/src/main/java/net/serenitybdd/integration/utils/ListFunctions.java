package net.serenitybdd.integration.utils;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFunctions {

    private ListFunctions(){}

    public static <T> List<T> concat(List<? extends T>... lists) {
        if (lists.length == 0) {
            return Collections.EMPTY_LIST;
        }

        List<T> combined = Lists.newArrayList();

        for (List<? extends T> list : lists) {
            combined.addAll(list);
        }

        return combined;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] combined = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, combined, first.length, second.length);

        return combined;
    }

    public static <T> T head(List<T> list) {
        return list.get(0);
    }

    public static <T> List<T> tail(List<T> list) {
        return ImmutableList.copyOf(list.subList(1, list.size()));
    }
}
