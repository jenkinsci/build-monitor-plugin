package net.serenitybdd.integration.utils;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class ListFunctions {
    public static <T> List<T> concat(List<? extends T> first, List<? extends T> second) {
        List<T> combined = new ArrayList<>(first.size() + second.size());
        combined.addAll(first);
        combined.addAll(second);

        return combined;
    }

    public static <T> T head(List<T> list) {
        return list.get(0);
    }

    public static List tail(List list) {
        return ImmutableList.copyOf(list.subList(1, list.size()));
    }
}
