package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.SerialisableAsJsonObjectCalled;

/**
 * @author Jan Molak
 */
public interface CandidateHeadline extends SerialisableAsJsonObjectCalled<Headline> {
    boolean isApplicableTo(JobView job);
}
