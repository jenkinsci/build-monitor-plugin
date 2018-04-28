package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import hudson.model.Result;
import hudson.model.Run;
import hudson.scm.ChangeLogSet;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.collect.Iterables.transform;

class BuildCulpritsWorkflowRun extends BuildCulpritsRetriever {

    BuildCulpritsWorkflowRun() {}

    @Override
    public Set<String> getCulprits(Run<?, ?> run) {
        WorkflowRun workflowRun = (WorkflowRun) run;
        Set<String> culprits = new TreeSet<String>();
        //Workaround while waiting for https://issues.jenkins-ci.org/browse/JENKINS-24141
        WorkflowRun previous = workflowRun.getPreviousCompletedBuild();
        if (workflowRun.isBuilding()) {
            //We are currently building so add culprits from previous build (if any)
            if (previous != null) {
                Result previousResult = previous.getResult();
                if (previousResult != null && previousResult.isWorseThan(Result.SUCCESS)) {
                    culprits.addAll(getCommitters(previous));
                }
            }
        }
        culprits.addAll(getCommitters(workflowRun));

        //Get culprits from earlier builds
        if (previous != null && previous.getPreviousNotFailedBuild() != null) {
            culprits.addAll(getCulpritsForRun(previous.getPreviousNotFailedBuild(), previous));
        }
        return culprits;
    }

    @Override
    protected Set<String> getCommittersForRun(Run<?, ?> run) {
        WorkflowRun workflowRun = (WorkflowRun) run;
        Set<String> committers = new TreeSet<String>();
        for (ChangeLogSet<? extends ChangeLogSet.Entry> changeLogSet : workflowRun.getChangeSets()) {
            Iterables
                .addAll(committers, transform(nonNullIterable(changeLogSet), new Function<ChangeLogSet.Entry, String>
                    () {
                    @Override
                    public String apply(@Nullable ChangeLogSet.Entry entry) {
                        return entry != null ? entry.getAuthor().getFullName() : null;
                    }
                }));
        }
        return committers;
    }

    private Set<String> getCulpritsForRun(WorkflowRun from, WorkflowRun to) {
        Set<String> culprits = new TreeSet<String>();
        WorkflowRun next = null;
        while (true) {
            next = next == null ? from.getNextBuild() : next.getNextBuild();
            if (next == null || next.getNumber() >= to.getNumber()) {
                break;
            }
            Iterables.addAll(culprits, getCommitters(next));
        }
        return culprits;
    }
}
