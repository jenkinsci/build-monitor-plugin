package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration;

abstract public class Duration {

    protected final long duration;

    public Duration(long milliseconds) {
        this.duration = milliseconds;
    }

    abstract public String toString();

    public boolean greaterThan(Duration otherDuration) {
        return duration > otherDuration.toLong();
    }

    private long toLong() {
        return duration;
    }
}