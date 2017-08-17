package com.smartcodeltd.jenkinsci.plugins.buildmonitor.status;

import com.google.common.base.Joiner;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline.WorkflowNodeTraversal;
import hudson.model.Run;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.List;

public class BuildStatusRetriever {

    private static final String Pipeline = "workflow-aggregator";
    private static final String Empty = "";

    public static String getBuildStatus(Run<?, ?> run) {
        StaticJenkinsAPIs staticJenkinsAPIs = new StaticJenkinsAPIs();

        if (staticJenkinsAPIs.hasPlugin(Pipeline) && run instanceof WorkflowRun) {
            return getPipelineStages((WorkflowRun) run);
        } else {
            return Empty;
        }
    }

    private static String getPipelineStages(WorkflowRun run) {
        FlowExecution execution = run.getExecution();

        if (execution != null) {
            WorkflowNodeTraversal traversal = new WorkflowNodeTraversal();
            traversal.start(execution.getCurrentHeads());

            List<String> stages = traversal.getStages();
            if (!stages.isEmpty()) {
                return "[" + Joiner.on(", ").join(stages) + "]";
            }
        }

        return Empty;
    }
}
