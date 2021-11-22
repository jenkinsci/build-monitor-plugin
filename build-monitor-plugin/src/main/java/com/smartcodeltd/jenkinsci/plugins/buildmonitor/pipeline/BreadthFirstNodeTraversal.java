package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class BreadthFirstNodeTraversal<N> {

    private final Queue<N> nodesToAccess;
    private final Set<String> stages;

    public BreadthFirstNodeTraversal() {
        this.nodesToAccess = new LinkedList<>();
        this.stages = new LinkedHashSet<>();
    }

    public void start(List<N> nodes) {
        nodesToAccess.addAll(nodes);
        while (!nodesToAccess.isEmpty()) {
            N node = nodesToAccess.remove();
            if (isStageStep(node)) {
                stages.add(getDisplayName(node));
            } else {
                nodesToAccess.addAll(getParents(node));
            }
        }
    }

    protected abstract boolean isStageStep(N node);

    protected abstract String getDisplayName(N node);

    protected abstract Collection<N> getParents(N node);

    public List<String> getStages() {
        return new ArrayList<>(stages);
    }
}