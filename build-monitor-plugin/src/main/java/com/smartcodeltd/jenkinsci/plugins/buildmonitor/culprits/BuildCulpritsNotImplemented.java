package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import hudson.model.Run;

import java.util.Collections;
import java.util.Set;

public class BuildCulpritsNotImplemented extends BuildCulpritsRetriever {

    BuildCulpritsNotImplemented() { }

    @Override
    public Set<String> getCulprits(Run<?, ?> run) {
        return Collections.emptySet();
    }

    @Override
    protected Set<String> getCommittersForRun(Run<?, ?> run) {
        return Collections.emptySet();
    }
}
