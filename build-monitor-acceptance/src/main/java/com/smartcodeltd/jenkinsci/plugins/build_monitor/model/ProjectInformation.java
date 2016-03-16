package com.smartcodeltd.jenkinsci.plugins.build_monitor.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.List;

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
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("status", status)
                .toString();
    }
}
