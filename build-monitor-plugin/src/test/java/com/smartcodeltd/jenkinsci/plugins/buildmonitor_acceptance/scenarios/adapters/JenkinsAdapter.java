package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.adapters;

import hudson.model.TopLevelItem;
import jenkins.model.Jenkins;

import java.io.IOException;

public class JenkinsAdapter implements ProjectCreator {
    private final Jenkins jenkins;

    public JenkinsAdapter(Jenkins jenkins) {
        this.jenkins = jenkins;
    }

    @Override
    public <T extends TopLevelItem> T createProject(Class<T> type, String name) throws IOException {
        return jenkins.createProject(type, name);
    }
}
