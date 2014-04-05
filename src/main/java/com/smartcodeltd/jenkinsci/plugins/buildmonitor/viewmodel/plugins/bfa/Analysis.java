package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.Augmentation;

import java.util.List;

public interface Analysis extends Augmentation {
    boolean foundKnownFailures();
    List<String> failures();
}
