package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import com.sonyericsson.jenkins.plugins.bfa.model.FoundFailureCause;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.User;
import hudson.plugins.claim.ClaimBuildAction;
import hudson.scm.ChangeLogSet;
import jenkins.model.CauseOfInterruption;
import jenkins.model.InterruptedBuildAction;
import org.powermock.api.mockito.PowerMockito;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author Jan Molak
 */
public class BuildStateRecipe implements Supplier<AbstractBuild<?, ?>> {

    private AbstractBuild<?, ?> build;

    public BuildStateRecipe() {
        build = mock(AbstractBuild.class);

        AbstractProject parent = mock(AbstractProject.class);
        when(build.getParent()).thenReturn(parent);
    }

    public BuildStateRecipe hasNumber(int number) {
        when(build.getNumber()).thenReturn(number);

        // see hudson.model.Run::getDisplayName
        return hasName("#" + number);
    }

    public BuildStateRecipe hasName(String name) {
        when(build.getDisplayName()).thenReturn(name);

        return this;
    }

    public BuildStateRecipe whichNumberIs(int number) {
        return hasNumber(number);
    }

    public BuildStateRecipe finishedWith(Result result) {
        when(build.getResult()).thenReturn(result);

        return this;
    }

    public BuildStateRecipe withChangesFrom(String... authors) {
        ChangeLogSet changeSet = changeSetBasedOn(entriesBy(authors));
        when(build.getChangeSet()).thenReturn(changeSet);

        // any methods that use getChangeSet as their source of data should be called normally
        // (build is a partial mock in this case)
        when(build.getCulprits()).thenCallRealMethod();

        return this;
    }

    public BuildStateRecipe succeededThanksTo(String... authors) {
        finishedWith(Result.SUCCESS);
        withChangesFrom(authors);

        return this;
    }

    public BuildStateRecipe wasBrokenBy(String... culprits) {
        finishedWith(Result.FAILURE);
        withChangesFrom(culprits);

        return this;
    }

    public BuildStateRecipe hasntStartedYet() {
        when(build.hasntStartedYet()).thenReturn(true);

        return this;
    }

    public BuildStateRecipe isStillBuilding() {
        when(build.isBuilding()).thenReturn(true);

        return this;
    }

    public BuildStateRecipe isStillUpdatingTheLog() {
        when(build.isLogUpdated()).thenReturn(true);

        return this;
    }

    public BuildStateRecipe startedAt(String time) throws Exception {
        long startedAt = new SimpleDateFormat("H:m:s").parse(time).getTime();

        Calendar timestamp = mock(Calendar.class);
        when(build.getTimestamp()).thenReturn(timestamp);

        when(timestamp.getTimeInMillis()).thenReturn(startedAt);

        return this;
    }

    public BuildStateRecipe took(int minutes) throws Exception{
        long duration = (long) minutes * 60 * 1000;

        when(build.getDuration()).thenReturn(duration);

        return this;
    }

    public BuildStateRecipe usuallyTakes(int minutes) throws Exception{
        long duration = (long) minutes * 60 * 1000;

        when(build.getEstimatedDuration()).thenReturn(duration);

        return this;
    }

    public BuildStateRecipe wasClaimedBy(String aPotentialHero, String reason) {
        final ClaimBuildAction action = claimBuildAction(aPotentialHero, reason);
        when(build.getAction(ClaimBuildAction.class)).thenReturn(action);

        return this;
    }

    private ClaimBuildAction claimBuildAction(String author, String reason) {
        ClaimBuildAction action = mock(ClaimBuildAction.class);
        when(action.isClaimed()).thenReturn(true);
        when(action.getClaimedByName()).thenReturn(author);
        when(action.getReason()).thenReturn(reason);

        return action;
    }

    public BuildStateRecipe wasAbortedBy(String username) {
        User user = userCalled(username);

        mockStatic(User.class);
        PowerMockito.when(User.get(user.getId())).thenReturn(user);

        final InterruptedBuildAction action = interruptedBuildAction(user);
        when(build.getAction(InterruptedBuildAction.class)).thenReturn(action);

        finishedWith(Result.ABORTED);

        return this;
    }

    private InterruptedBuildAction interruptedBuildAction(User user) {
        List<CauseOfInterruption> causes = Lists.<CauseOfInterruption>newArrayList(
                new CauseOfInterruption.UserInterruption(user)
        );

        InterruptedBuildAction action = mock(InterruptedBuildAction.class);
        when(action.getCauses()).thenReturn(causes);

        return action;
    }

    public BuildStateRecipe knownProblems(String... failures) {
        final FailureCauseBuildAction action = failureCauseBuildAction(failures);
        when(build.getAction(FailureCauseBuildAction.class)).thenReturn(action);

        return this;
    }

    private FailureCauseBuildAction failureCauseBuildAction(String... FailureNames) {
        FailureCauseBuildAction action = mock(FailureCauseBuildAction.class);
        List<FoundFailureCause> items = new ArrayList<FoundFailureCause>();
        for( String name : FailureNames ) {
            items.add(failure(name));
        }
        when(action.getFoundFailureCauses()).thenReturn(items);

        return action;
    }

    private FoundFailureCause failure(String name) {
        FoundFailureCause failure = mock(FoundFailureCause.class);
        when(failure.getDescription()).thenReturn(name);
        return failure;
    }

    public BuildStateRecipe and() {
        return this;
    }

    @Override
    public AbstractBuild get() {
        return build;
    }

    // todo: replace mock user with userCalled
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

    private User userCalled(String name) {
        User user = mock(User.class);
        when(user.getId()).thenReturn(name.toLowerCase());
        when(user.getFullName()).thenReturn(name);

        return user;
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