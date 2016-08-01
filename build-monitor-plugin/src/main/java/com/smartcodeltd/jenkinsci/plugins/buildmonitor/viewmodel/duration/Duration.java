package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration;

public abstract class Duration {

    protected final long duration;

    public Duration(long milliseconds) {
        this.duration = milliseconds;
    }

    abstract public String value();

    public boolean greaterThan(Duration otherDuration) {
        return duration > otherDuration.toLong();
    }

    private long toLong() {
        return duration;
    }
}