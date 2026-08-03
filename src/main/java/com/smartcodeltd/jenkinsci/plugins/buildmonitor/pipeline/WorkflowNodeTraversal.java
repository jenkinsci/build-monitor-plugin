package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import java.util.Collection;
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.support.steps.StageStep;

public class WorkflowNodeTraversal extends BreadthFirstNodeTraversal<FlowNode> {

    @Override
    protected boolean isStageStep(FlowNode node) {
        if (node instanceof StepStartNode stepStartNode) {
            StepDescriptor d = stepStartNode.getDescriptor();
            if (d != null) {
                return d.isSubTypeOf(StageStep.class);
            }
        }
        return false;
    }

    @Override
    protected String getDisplayName(FlowNode node) {
        return node.getDisplayName();
    }

    @Override
    protected Collection<FlowNode> getParents(FlowNode node) {
        return node.getParents();
    }
}
