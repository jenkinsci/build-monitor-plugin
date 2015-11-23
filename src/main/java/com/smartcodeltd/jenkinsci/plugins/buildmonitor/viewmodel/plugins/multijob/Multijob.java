package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.Augmentation;

public interface Multijob extends Augmentation {
    public int phase();
    public int jobsinphase();
    public int numphases();
}
