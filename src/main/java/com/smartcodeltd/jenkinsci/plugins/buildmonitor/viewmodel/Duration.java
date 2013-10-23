package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

class Duration {//todo: extract the Duration class, or move it to a BuildView class

    private final long duration;

    private final static long MILLISECOND = 1;
    private final static long SECONDS = 1000 * MILLISECOND;
    private final static long MINUTES = 60 * SECONDS;
    private final static long HOURS = 60 * MINUTES;

    public Duration(long milliseconds) {
        this.duration = milliseconds;
    }

    public long hours() {
        return duration / HOURS;
    }

    public long minutes() {
        return (duration - (hours() * HOURS)) / MINUTES;
    }

    public long seconds() {
        return (duration - (hours() * HOURS) - (minutes() * MINUTES)) / SECONDS;
    }

    public boolean greaterThan(Duration otherDuration) {
        return duration > otherDuration.toLong();
    }

    public String toString() {
        String formatted;

        formatted  = hours() > 0   ? hours()   + "h " : "";
        formatted += minutes() > 0 ? minutes() + "m " : "";
        formatted += seconds() + "s";

        return formatted;
    }


    private long toLong() {
        return duration;
    }
}