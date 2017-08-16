package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Job;

import java.util.List;

/**
 * @author Jan Molak
 */
public interface Feature<JSON extends Object> extends SerialisableAsJsonObjectCalled<JSON> {
    <F extends Feature> F of(JobView jobView);

    List<JobView> jobs(JobView parentJobView, Job<?, ?> parentJob);
}
