package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob;

public class NoMultijob implements Multijob {
    @Override
    public int phase() {
        return 0;
    }

    @Override
    public int jobsinphase() {
        return 1;
    }

    @Override
    public int numphases() {
        return 1;
    }
}
