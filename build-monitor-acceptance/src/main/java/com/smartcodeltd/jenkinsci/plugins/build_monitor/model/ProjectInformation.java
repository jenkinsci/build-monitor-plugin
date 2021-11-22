package com.smartcodeltd.jenkinsci.plugins.build_monitor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectInformation {
    private final String name;
    private final List<ProjectStatus> status;

    public ProjectInformation(String name, List<ProjectStatus> status) {
        this.name = name;
        this.status = status;
    }

    public String name() {
        return name;
    }

    public List<ProjectStatus> status() {
        return Collections.unmodifiableList(new ArrayList<>(status));
    }

    @Override
    public String toString() {
        return String.format("ProjectInformation{name=%s, status={%s}}", name, status.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
