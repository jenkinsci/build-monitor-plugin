package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim;

public class NotClaimed implements Claim {
    @Override
    public boolean wasMade() {
        return false;
    }

    @Override
    public String author() {
        return null;
    }

    @Override
    public String reason() {
        return null;
    }

    @Override
    public String toString() {
        return "Not claimed";
    }
}
