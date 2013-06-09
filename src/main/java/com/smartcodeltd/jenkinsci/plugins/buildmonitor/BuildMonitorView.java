package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import hudson.Extension;
import hudson.model.ListView;
import hudson.model.ViewDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class BuildMonitorView extends ListView {

    /**
     * @param name  Name of the view
     */
    @DataBoundConstructor
    public BuildMonitorView(String name) {
        super(name);
    }

    @Extension
    public static final class Descriptor extends ViewDescriptor {
        public Descriptor() {
            super(BuildMonitorView.class);
        }

        @Override
        public String getDisplayName() {
            return "Build Monitor";
        }
    }
}
