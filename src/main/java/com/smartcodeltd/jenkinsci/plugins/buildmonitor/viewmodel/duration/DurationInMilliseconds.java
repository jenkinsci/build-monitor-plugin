package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration;

public class DurationInMilliseconds extends Duration {

    public DurationInMilliseconds(long milliseconds) {
        super(milliseconds);
    }

    @Override
    public String value() {
        return String.valueOf(duration);
    }

    @Override
    public String toString() {
        return value();
    }
}
