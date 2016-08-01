package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.SerialisableAsJsonObjectCalled;

/**
 * @author Jan Molak
 */
public class NoHeadline implements SerialisableAsJsonObjectCalled<Headline> {

    @Override
    public Headline asJson() {
        return new Headline("");
    }
}
