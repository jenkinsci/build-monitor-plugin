package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons;

/**
 * Contains methods used to filter the job name with specified prefix, suffix and regex
 */
public class StripMostCommonSuffix {
    private StripMostCommonSuffixConfig conf;

    public StripMostCommonSuffix(StripMostCommonSuffixConfig conf) {
        this.conf = conf;
    }


    public boolean getSuffix(){
        return conf.suffix;
    }
}