package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.User;
import hudson.scm.ChangeLogSet;

import java.util.Set;
import java.util.TreeSet;

import static com.google.common.collect.Iterables.transform;

class BuildCulpritsAbstractBuild extends BuildCulpritsRetriever {

    BuildCulpritsAbstractBuild() {}

    @Override
    public Set<String> getCulprits(Run<?, ?> run) {
        AbstractBuild<?, ?> abstractBuild = (AbstractBuild<?, ?>) run;
        Set<String> culprits = new TreeSet<String>();
        Iterable<String> forAbstractBuild = transform(abstractBuild.getCulprits(), new Function<User, String>() {
            @Override
            public String apply(User culprit) {
                return culprit.getFullName();
            }
        });
        Iterables.addAll(culprits, forAbstractBuild);
        return culprits;
    }


    @Override
    protected Set<String> getCommittersForRun(Run<?, ?> run) {
        AbstractBuild<?, ?> abstractBuild = (AbstractBuild<?, ?>) run;
        Set<String> committers = new TreeSet<String>();
        Iterable<String> iterable = transform(nonNullIterable(((AbstractBuild<?, ?>) abstractBuild)
            .getChangeSet()), new Function<ChangeLogSet.Entry, String>() {
            @Override
            public String apply(ChangeLogSet.Entry entry) {
                return entry.getAuthor().getFullName();
            }
        });
        Iterables.addAll(committers, iterable);
        return committers;
    }
}
