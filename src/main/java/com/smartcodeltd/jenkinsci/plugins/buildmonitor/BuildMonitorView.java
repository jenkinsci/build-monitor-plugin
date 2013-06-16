package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.ListView;
import hudson.model.TopLevelItem;
import hudson.model.ViewDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public List<JobView> getJobs() {
        List<JobView> jobs = new ArrayList<JobView>();

        for (TopLevelItem item : super.getItems()) {
            if (item instanceof AbstractProject) {
                AbstractProject project = (AbstractProject) item;
                if (! project.isDisabled()) {
                    jobs.add(JobView.of(project));
                }
            }
        }

        return jobs;
    }
}
