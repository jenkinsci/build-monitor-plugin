package com.smartcodeltd.jenkinsci.plugins.buildmonitor.installation;

public enum Audience {
    BuildMonitorDevelopers("developers"),
    EndUsers("users");

    private final String name;

    Audience(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
