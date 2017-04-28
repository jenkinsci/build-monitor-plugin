package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons.*;

public class shouldStripMostCommonSuffix implements Feature<StrippedMostCommonSuffix> {
    private JobView job;
    private StripMostCommonSuffixConfig config;


    public shouldStripMostCommonSuffix(StripMostCommonSuffixConfig Strip) {
        this.config = Strip;
    }

    @Override
    public shouldStripMostCommonSuffix of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public StrippedMostCommonSuffix asJson() {
        StripMostCommonSuffix coms = new StripMostCommonSuffix(config);
        return new StrippedMostCommonSuffix(coms.getSuffix());
    }
}