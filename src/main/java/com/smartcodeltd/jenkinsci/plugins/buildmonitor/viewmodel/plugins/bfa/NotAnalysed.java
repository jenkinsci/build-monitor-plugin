package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa;

import java.util.Collections;
import java.util.List;

public class NotAnalysed implements Analysis {
    @Override
    public boolean foundKnownFailures() {
        return false;
    }

    @Override
    public List<String> failures() {
        return Collections.emptyList();
    }

    public String toString() {
        return "No known failures";
    }
}
