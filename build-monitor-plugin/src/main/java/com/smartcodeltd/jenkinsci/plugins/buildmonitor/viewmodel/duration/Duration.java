package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration;

public abstract class Duration {

    protected final long duration;

    public Duration(long milliseconds) {
        this.duration = milliseconds;
    }

    public abstract String toString();

    public boolean greaterThan(Duration otherDuration) {
        return duration > otherDuration.toLong();
    }

    private long toLong() {
        return duration;
    }
}