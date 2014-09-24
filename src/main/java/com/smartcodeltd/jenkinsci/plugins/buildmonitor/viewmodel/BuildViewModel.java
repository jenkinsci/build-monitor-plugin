package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.Result;

import java.util.List;
import java.util.Set;

public interface BuildViewModel {
    public String name();
    public String url();
    public Result result();

    public boolean isRunning();
    public Duration elapsedTime();
    public Duration duration();
    public Duration estimatedDuration();
    public int progress();

    public boolean hasPreviousBuild();
    public BuildViewModel previousBuild();

    public Set<String> culprits();

    boolean isClaimed();
    String claimant();
    String reasonForClaim();

    boolean hasKnownFailures();

    List<String> knownFailures();
    

    public int failureCount();
    
    public String changeNumber();
    public boolean isEmptyChangeSet();
	public int changeSetCount();
	public String changeString();
}
