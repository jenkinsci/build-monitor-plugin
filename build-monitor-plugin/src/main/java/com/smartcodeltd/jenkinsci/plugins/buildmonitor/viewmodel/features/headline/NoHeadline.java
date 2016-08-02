package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

/**
 * @author Jan Molak
 */
public class NoHeadline implements CandidateHeadline {

    @Override
    public boolean isApplicableTo(JobView job) {
        return true;
    }

    @Override
    public Headline asJson() {
        return new Headline("");
    }
}
