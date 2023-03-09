package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import hudson.model.Run;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.List;

public class PipelineHelper {

    private static final String PIPELINE_PLUGIN = "workflow-job";
    private static final String WORKFLOW_RUN_CLASS_NAME = "org.jenkinsci.plugins.workflow.job.WorkflowRun";
    private static Boolean hasWorkflowClass = null;
    private static Class<?> workflowRunClass = null;

    private PipelineHelper() {}

    public static boolean isWorkflowRun(Run<?, ?> build, StaticJenkinsAPIs staticJenkinsAPIs) {
        //Cache class lookup
        if (hasWorkflowClass == null) {
            if (!staticJenkinsAPIs.hasPlugin(PIPELINE_PLUGIN)) {
                hasWorkflowClass = false;
                return false;
            }
            try {
                workflowRunClass = Class.forName(WORKFLOW_RUN_CLASS_NAME);
                hasWorkflowClass = true;
            } catch (ClassNotFoundException e) {
                hasWorkflowClass = false;
                return false;
            }
        }
        if (hasWorkflowClass) {
            return workflowRunClass.isInstance(build);
        }
        return false;
    }

    public static List<String> getPipelines(Run<?, ?> run) {
        WorkflowRun currentBuild = (WorkflowRun) run;
        FlowExecution execution = currentBuild.getExecution();
        if (execution != null) {
            WorkflowNodeTraversal traversal = new WorkflowNodeTraversal();
            traversal.start(execution.getCurrentHeads());
            return traversal.getStages();
        }
        return List.of();
    }
}
