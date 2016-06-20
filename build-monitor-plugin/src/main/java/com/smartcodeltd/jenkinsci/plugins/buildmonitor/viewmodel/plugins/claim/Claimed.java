package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim;

import hudson.plugins.claim.ClaimBuildAction;

public class Claimed implements Claim {
    private final ClaimBuildAction action;

    public Claimed(ClaimBuildAction action) {
        this.action = action;
    }

    @Override
    public boolean wasMade() {
        return action.isClaimed();
    }

    @Override
    public String author() {
        return action.getClaimedByName();
    }

    @Override
    public String reason() {
        return action.getReason();
    }

    @Override
    public String toString() {
        return String.format("Claimed by \"%s\": \"%s\"", author(), reason());
    }
}
