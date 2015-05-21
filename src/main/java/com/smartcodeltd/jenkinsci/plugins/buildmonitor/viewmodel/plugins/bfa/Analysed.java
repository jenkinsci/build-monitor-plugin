package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa;

import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import com.sonyericsson.jenkins.plugins.bfa.model.FoundFailureCause;

import java.util.ArrayList;
import java.util.List;

public class Analysed implements Analysis {
    private final FailureCauseBuildAction action;

    public Analysed( FailureCauseBuildAction action ) {
        this.action = action;
    }

    @Override
    public boolean foundKnownFailures() {
        return ! action.getFoundFailureCauses().isEmpty();
    }

    @Override
    public List<String> failures() {
        List<String> failures = new ArrayList<String>(action.getFoundFailureCauses().size());
        for (FoundFailureCause failure : action.getFoundFailureCauses()) {
            failures.add(failure.getDescription());
        }
        return failures;
    }

    public String toString() {
        return String.format("Failed with %s",failures());
    }

}
