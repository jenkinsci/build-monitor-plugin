package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class BreadthFirstNodeTraversalTest {
    @Test
    public void traversal_should_identify_the_stage_nodes() {
        TestNode head = normalNode("A1",
                withParent(stageNode("B1",
                        withParent(normalNode("C1")),
                        withParent(normalNode("C2")))),
                withParent(stageNode("B2",
                        withParent(normalNode("C3")),
                        withParent(normalNode("C4")))),
                withParent(normalNode("B3")));

        List<String> stages = findStages(head);

        assertThat(stages, containsInAnyOrder("B1", "B2"));
    }

    @Test
    public void stages_should_not_be_repeated() {
        TestNode head = normalNode("A1",
                withParent(stageNode("B1")),
                withParent(stageNode("B1")));

        List<String> stages = findStages(head);

        assertThat(stages, contains("B1"));
    }

    private List<String> findStages(TestNode head) {
        BreadthFirstNodeTraversal<TestNode> traversal = new BreadthFirstTestNodeTraversal();
        traversal.start(Collections.singletonList(head));
        return traversal.getStages();
    }

    private enum Type {
        STAGE, NORMAL
    }

    private static class TestNode {
        private final String displayName;
        private final Type type;
        private final List<TestNode> parents;

        TestNode(String displayName, Type type, TestNode[] parents) {
            this.displayName = displayName;
            this.type = type;
            this.parents = Arrays.asList(parents);
        }

        String getDisplayName() {
            return displayName;
        }

        Type getType() {
            return type;
        }

        List<TestNode> getParents() {
            return parents;
        }
    }

    private static class BreadthFirstTestNodeTraversal extends BreadthFirstNodeTraversal<TestNode> {
        @Override
        protected boolean isStageStep(TestNode node) {
            return node.getType() == Type.STAGE;
        }

        @Override
        protected String getDisplayName(TestNode node) {
            return node.getDisplayName();
        }

        @Override
        protected Collection<TestNode> getParents(TestNode node) {
            return node.getParents();
        }
    }

    private static TestNode stageNode(String displayName, TestNode... parents) {
        return new TestNode(displayName, Type.STAGE, parents);
    }

    private static TestNode normalNode(String displayName, TestNode... parents) {
        return new TestNode(displayName, Type.NORMAL, parents);
    }

    private static TestNode withParent(TestNode testNode) {
        return testNode;
    }
}