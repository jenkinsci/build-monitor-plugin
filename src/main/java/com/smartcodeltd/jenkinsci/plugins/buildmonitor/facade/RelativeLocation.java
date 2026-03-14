package com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import hudson.Functions;
import hudson.model.ItemGroup;
import hudson.model.Job;
import hudson.model.View;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest2;

public class RelativeLocation {
    private final Job job;

    public static RelativeLocation of(Job job) {
        return new RelativeLocation(job);
    }

    public String name() {
        ItemGroup ig = null;

        StaplerRequest2 request = Stapler.getCurrentRequest2();
        for (Ancestor a : request.getAncestors()) {
            if (a.getObject() instanceof BuildMonitorView) {
                ig = ((View) a.getObject()).getOwner().getItemGroup();
            }
        }

        return Functions.getRelativeDisplayNameFrom(job, ig);
    }

    public String url() {
        return Functions.getRelativeLinkTo(job);
    }

    private RelativeLocation(Job job) {
        this.job = job;
    }
}
