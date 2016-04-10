package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration;

// todo: is this still needed? backend could pass the duration in milliseconds to the frontend, which in turn could make it human-readable
public class HumanReadableDuration extends Duration {
    private final static long MILLISECOND = 1;
    private final static long SECONDS = 1000 * MILLISECOND;
    private final static long MINUTES = 60 * SECONDS;
    private final static long HOURS = 60 * MINUTES;

    public HumanReadableDuration(long milliseconds) {
        super(milliseconds);
    }

    private long hours() {
        return duration / HOURS;
    }

    private long minutes() {
        return (duration - (hours() * HOURS)) / MINUTES;
    }

    private long seconds() {
        return (duration - (hours() * HOURS) - (minutes() * MINUTES)) / SECONDS;
    }

    @Override
    public String toString() {
        String formatted;

        formatted  = hours() > 0   ? hours()   + "h " : "";
        formatted += minutes() > 0 ? minutes() + "m " : "";
        formatted += seconds() + "s";

        return formatted;
    }
}
