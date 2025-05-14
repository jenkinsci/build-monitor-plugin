package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import com.cloudbees.hudson.plugins.folder.Folder;
import hudson.model.TopLevelItem;
import java.io.IOException;
import org.jvnet.hudson.test.JenkinsRule;

public class FolderUtils {

    public static FluentFolder createFolder(JenkinsRule jenkins, String folderName) {
        try {
            Folder folder = jenkins.createProject(Folder.class, folderName);

            return new FluentFolder(jenkins, folder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class FluentFolder {

        private final JenkinsRule jenkins;

        private final Folder folder;

        public FluentFolder(JenkinsRule jenkins, Folder folder) {
            this.jenkins = jenkins;
            this.folder = folder;
        }

        public FolderUtils.FluentFolder addJobs(TopLevelItem... project) {
            try {
                for (TopLevelItem topLevelItem : project) {
                    folder.add(topLevelItem, topLevelItem.getName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public Folder get() {
            return folder;
        }
    }
}
