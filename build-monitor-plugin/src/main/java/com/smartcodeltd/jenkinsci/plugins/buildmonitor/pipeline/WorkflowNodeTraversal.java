package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.support.steps.StageStep;

import java.util.Collection;

public class WorkflowNodeTraversal extends BreadthFirstNodeTraversal<FlowNode> {
    @Override
    protected boolean isStageStep(FlowNode node) {
        return node instanceof StepStartNode
                && ((StepStartNode) node).getDescriptor().isSubTypeOf(StageStep.class);
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
