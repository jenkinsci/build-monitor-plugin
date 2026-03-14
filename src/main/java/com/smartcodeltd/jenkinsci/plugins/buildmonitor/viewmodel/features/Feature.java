package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

/**
 * @author Jan Molak
 */
public interface Feature<JSON> extends SerialisableAsJsonObjectCalled<JSON> {
    <F extends Feature> F of(JobView jobView);
}
