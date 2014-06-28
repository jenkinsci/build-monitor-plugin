package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.adapters;

import com.cloudbees.hudson.plugins.folder.Folder;
import hudson.model.TopLevelItem;

import java.io.IOException;

public class FolderAdapter implements ProjectCreator {
    private final Folder folder;

    public FolderAdapter(Folder folder) {
        this.folder = folder;
    }

    @Override
    public <T extends TopLevelItem> T createProject(Class<T> type, String name) throws IOException {
        return this.folder.createProject(type, name);
    }
}
