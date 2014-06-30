package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.adapters;

import hudson.model.TopLevelItem;

import java.io.IOException;

public interface ProjectCreator {
    public <T extends TopLevelItem> T createProject(Class<T> type, String name) throws IOException;
}
