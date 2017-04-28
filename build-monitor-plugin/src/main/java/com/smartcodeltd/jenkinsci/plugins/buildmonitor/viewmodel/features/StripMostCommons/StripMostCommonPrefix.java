package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.StripMostCommons;

/**
 * Contains methods used to filter the job name with specified prefix, suffix and regex
 */
public class StripMostCommonPrefix {
    private StripMostCommonPrefixConfig conf;

    public StripMostCommonPrefix(StripMostCommonPrefixConfig conf) {
        this.conf = conf;
    }

    public boolean getPrefix(){
        //Boolean.toString(conf.prefix)
        return conf.prefix;
    }
}