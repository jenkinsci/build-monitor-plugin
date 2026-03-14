package com.smartcodeltd.jenkinsci.plugins.buildmonitor.build;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.Extension;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class GetLastBuild implements GetBuildViewModel {

    @Override
    public BuildViewModel from(JobView job) {
        return job.lastBuild();
    }

    @DataBoundConstructor
    public GetLastBuild() {}

    @Extension
    public static class GetLastCompletedBuildDescriptor extends Descriptor<GetBuildViewModel> {}
}
