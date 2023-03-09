package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.User;
import hudson.scm.ChangeLogSet;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class BuildCulpritsAbstractBuild extends BuildCulpritsRetriever {

    BuildCulpritsAbstractBuild() {}

    @Override
    public Set<String> getCulprits(Run<?, ?> run) {
        AbstractBuild<?, ?> abstractBuild = (AbstractBuild<?, ?>) run;
        return abstractBuild.getCulprits().stream()
                .map(User::getFullName)
                .collect(Collectors.toCollection(TreeSet::new));
    }


    @Override
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
    protected Set<String> getCommittersForRun(Run<?, ?> run) {
        AbstractBuild<?, ?> abstractBuild = (AbstractBuild<?, ?>) run;
        ChangeLogSet<? extends ChangeLogSet.Entry> cs = abstractBuild.getChangeSet();
        if (cs == null) {
            return Set.of();
        }
        return StreamSupport.stream(cs.spliterator(), false)
                .map(entry -> entry.getAuthor().getFullName())
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
