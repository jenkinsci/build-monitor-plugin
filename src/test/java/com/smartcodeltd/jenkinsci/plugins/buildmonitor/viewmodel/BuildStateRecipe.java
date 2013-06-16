package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.User;
import hudson.scm.ChangeLogSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuildStateRecipe {

    private AbstractBuild<?, ?> build;

    public BuildStateRecipe() {
        build = mock(AbstractBuild.class);
    }

    public BuildStateRecipe thatFinishedWith(Result result) {
        when(build.getResult()).thenReturn(result);

        return this;
    }

    public BuildStateRecipe withChangesFrom(String... authors) {
        ChangeLogSet changeSet = changeSetBasedOn(entriesBy(authors));
        when(build.getChangeSet()).thenReturn(changeSet);

        // any methods that use getChangeSet as their source of data should be called normally (build is a partial mock)
        when(build.getCulprits()).thenCallRealMethod();

        return this;
    }

    public BuildStateRecipe thatHasntStartedYet() {
        when(build.hasntStartedYet()).thenReturn(true);

        return this;
    }

    public BuildStateRecipe thatIsStillBuilding() {
        when(build.isBuilding()).thenReturn(true);

        return this;
    }

    public BuildStateRecipe thatIsStillUpdatingTheLog() {
        when(build.isLogUpdated()).thenReturn(true);

        return this;
    }

    public AbstractBuild execute() {
        return build;
    }


    private List<ChangeLogSet.Entry> entriesBy(String... authors) {
        List<ChangeLogSet.Entry> entries = new ArrayList<ChangeLogSet.Entry>();

        for (String name : authors) {
            User author = mock(User.class);
            ChangeLogSet.Entry entry = mock(ChangeLogSet.Entry.class);

            when(author.getFullName()).thenReturn(name);
            when(entry.getAuthor()).thenReturn(author);

            entries.add(entry);
        }
        return entries;
    }

    private ChangeLogSet changeSetBasedOn(final List<ChangeLogSet.Entry> entries) {
        return new ChangeLogSet<ChangeLogSet.Entry>(null) {
            @Override
            public boolean isEmptySet() {
                return false;
            }

            public Iterator<Entry> iterator() {
                return entries.iterator();
            }
        };
    }
}
