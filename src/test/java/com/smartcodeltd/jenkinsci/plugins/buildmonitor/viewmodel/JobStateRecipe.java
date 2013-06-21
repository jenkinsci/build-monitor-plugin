package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.AbstractBuild;
import hudson.model.Job;

import java.util.Stack;

import static org.mockito.Mockito.*;

public class JobStateRecipe {
    private Job<?, ?> job;
    private Stack<AbstractBuild> buildHistory = new Stack<AbstractBuild>();

    public JobStateRecipe() {
        job = mock(Job.class, RETURNS_DEEP_STUBS);
    }

    public JobStateRecipe withName(String name) {
        when(job.getName()).thenReturn(name);

        return this;
    }

    public JobStateRecipe whereTheCurrentBuildNumberIs(int number) {
        return updatedWithOnlyOneHistoryEntryFor(a(build().whichNumberIs(number)));
    }

    public JobStateRecipe whereTheLast(BuildStateRecipe recipe) {
        return updatedWithOnlyOneHistoryEntryFor(recipe.execute());
    }

    public JobStateRecipe andThePrevious(BuildStateRecipe recipe) {
        return updatedWithEarliestHistoryEntryFor(recipe.execute());
    }

    @SuppressWarnings("unchecked")
    public Job<?, ?> execute() {
        AbstractBuild earlierBuild, earliestBuild;

        // link "previous" builds ...
        while (buildHistory.size() > 1) {
            earliestBuild = buildHistory.pop();
            earlierBuild  = buildHistory.peek();

            when(earlierBuild.getPreviousBuild()).thenReturn(earliestBuild);
        }

        // pick the first build from the build history and make it the "last build"
        if (buildHistory.size() == 1) {
            when(job.getLastBuild()).thenReturn(buildHistory.pop());
        }

        return job;
    }

    private JobStateRecipe updatedWithEarliestHistoryEntryFor(AbstractBuild build) {
        buildHistory.push(build);

        return this;
    }

    private JobStateRecipe updatedWithOnlyOneHistoryEntryFor(AbstractBuild build) {
        buildHistory.clear();

        return updatedWithEarliestHistoryEntryFor(build);
    }

    /*
     * Syntactic sugar
     */

    private BuildStateRecipe build() {
        return new BuildStateRecipe();
    }

    private AbstractBuild<?, ?> a(BuildStateRecipe recipe) {
        return recipe.execute();
    }
}