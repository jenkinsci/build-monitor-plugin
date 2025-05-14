package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import hudson.model.TopLevelItem;
import java.io.IOException;
import org.jvnet.hudson.test.JenkinsRule;

public class BuildMonitorViewUtils {

    public static FluentBuildMonitorView createBuildMonitorView(JenkinsRule jenkins, String name) {
        try {
            BuildMonitorView view = new BuildMonitorView(name, null);
            jenkins.getInstance().addView(view);
            return new FluentBuildMonitorView(
                    (BuildMonitorView) jenkins.getInstance().getView(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class FluentBuildMonitorView {
        private final BuildMonitorView view;

        public FluentBuildMonitorView(BuildMonitorView view) {
            this.view = view;
        }

        public FluentBuildMonitorView addJobs(TopLevelItem... project) {
            try {
                for (TopLevelItem topLevelItem : project) {
                    view.add(topLevelItem);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public BuildMonitorView get() {
            return view;
        }
    }
}
