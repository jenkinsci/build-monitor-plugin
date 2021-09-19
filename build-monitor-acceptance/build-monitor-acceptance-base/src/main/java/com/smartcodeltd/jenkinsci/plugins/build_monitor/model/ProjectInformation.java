package com.smartcodeltd.jenkinsci.plugins.build_monitor.model;

import com.google.common.collect.ImmutableList;

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
        return ImmutableList.copyOf(status);
    }

    @Override
    public String toString() {
        return "ProjectInformation{name=%s, status={%s}}".format(name, status.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
