package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import hudson.model.TopLevelItem;
import java.io.IOException;
import org.jvnet.hudson.test.JenkinsRule;

public class BuildMonitorViewUtils {

    public static BuildMonitorView createBuildMonitorView(JenkinsRule jenkins, String name) {
        try {
            jenkins.getInstance().addView(new BuildMonitorView(name, null));
            return (BuildMonitorView) jenkins.getInstance().getView(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BuildMonitorView addProjectToView(TopLevelItem project, BuildMonitorView view) {
        try {
            view.add(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
}
