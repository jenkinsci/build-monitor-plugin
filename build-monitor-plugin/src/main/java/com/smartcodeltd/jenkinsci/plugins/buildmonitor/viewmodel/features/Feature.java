package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

/**
 * @author Jan Molak
 */
public interface Feature {
    <F extends Feature> F of(JobView jobView);

    Object asJson();
}
