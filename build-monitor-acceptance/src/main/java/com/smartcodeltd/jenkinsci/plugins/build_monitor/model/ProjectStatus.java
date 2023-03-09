package com.smartcodeltd.jenkinsci.plugins.build_monitor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public enum ProjectStatus {
    Successful("successful"),
    Failing("failing"),
    Unknown("unknown"),
    Claimed("claimed");

    private final String value;

    ProjectStatus(String value) {
        this.value = value;
    }


    // ---- todo: extract

    // todo: can I use https://github.com/serenity-bdd/serenity-web-todomvc-journey/blob/e092e3a90a280aa16b0c1458f0ac7d36fe833f52/src/main/java/net/serenitybdd/demos/todos/questions/CurrentFilter.java ?
    // asEnum thingy

    public static List<ProjectStatus> fromMultiple(String cssClasses) {
        // todo: Java 8?
        List<ProjectStatus> statuses = new ArrayList<>();

        for (String statusClass : projectStatusClasses()) {
            if (setOf(split(cssClasses)).contains(statusClass)) {
                statuses.add(ProjectStatus.from(statusClass));
            }
        }

        return statuses;
    }

    public static ProjectStatus from(@NotNull String cssClass) {

        for (ProjectStatus status : ProjectStatus.values()) {
            if (cssClass.equalsIgnoreCase(status.value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format("'%s' is not a recognised value of the ProjectStatus enum", cssClass));
    }

    // todo: Java 8?

    private static Set<String> projectStatusClasses() {
        return setOf(stringRepresentationsOf(EnumSet.allOf(ProjectStatus.class)));
    }

    private static List<String> split(String spaceSeparatedItems) {
        return Stream.of(spaceSeparatedItems.split("\\s+")).collect(Collectors.toList());
    }

    private static <T> Set<T> setOf(List<T> items) {
        return Set.copyOf(items);
    }

    private static <T> List<String> stringRepresentationsOf(Collection<T> items) {
        return items.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return value;
    }
}
