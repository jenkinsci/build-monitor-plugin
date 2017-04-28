package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons.StripMostCommonPrefix;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons.StripMostCommonPrefixConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons.StrippedMostCommonPrefix;

public class shouldStripMostCommonPrefix implements Feature<StrippedMostCommonPrefix> {
    private JobView job;
    private StripMostCommonPrefixConfig config;


    public shouldStripMostCommonPrefix(StripMostCommonPrefixConfig stripMostCommonPrefixConfig) {
        this.config = stripMostCommonPrefixConfig;
    }

    @Override
    public shouldStripMostCommonPrefix of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public StrippedMostCommonPrefix asJson() {
        StripMostCommonPrefix coms = new StripMostCommonPrefix(config);
        return new StrippedMostCommonPrefix(coms.getPrefix());
    }
}