package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import hudson.model.Result;
import hudson.model.Run;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

class BuildCulpritsWorkflowRun extends BuildCulpritsRetriever {

    BuildCulpritsWorkflowRun() {}

    @Override
    public Set<String> getCulprits(Run<?, ?> run) {
        WorkflowRun workflowRun = (WorkflowRun) run;
        Set<String> culprits = new TreeSet<>();
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
        return workflowRun.getChangeSets().stream()
                .filter(Objects::nonNull)
                .flatMap(changeLogSet -> StreamSupport.stream(changeLogSet.spliterator(), false))
                .map(entry -> entry != null ? entry.getAuthor().getFullName() : null)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private Set<String> getCulpritsForRun(WorkflowRun from, WorkflowRun to) {
        Set<String> culprits = new TreeSet<>();
        WorkflowRun next = null;
        while (true) {
            next = next == null ? from.getNextBuild() : next.getNextBuild();
            if (next == null || next.getNumber() >= to.getNumber()) {
                break;
            }
            culprits.addAll(getCommitters(next));
        }
        return culprits;
    }
}
