package com.smartcodeltd.jenkinsci.plugins.buildmonitor.build;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.BuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

public interface GetBuildViewModel extends Describable<GetBuildViewModel> {

    BuildViewModel from(JobView job);

    @Override
    public default Descriptor<GetBuildViewModel> getDescriptor() {
        return Jenkins.get().getDescriptor(this.getClass());
    }
}
