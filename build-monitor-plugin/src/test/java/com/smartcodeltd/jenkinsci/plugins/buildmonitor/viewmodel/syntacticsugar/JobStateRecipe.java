package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.AbstractBuild;
import hudson.model.Job;
import hudson.model.ViewJob;
import hudson.util.RunList;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Jan Molak
 */
public class JobStateRecipe implements Supplier<Job<?,?>> {
    private Job<?, ?> job;
    private RunList<?> runList;
    private Stack<AbstractBuild> buildHistory = new Stack<>();
    private List<AbstractBuild> allBuilds = new ArrayList<>();

    public JobStateRecipe() {
        job = mock(Job.class);
        runList = mock(RunList.class);

        lenient().when(job.isBuildable()).thenReturn(Boolean.TRUE);
    }

    public JobStateRecipe withName(String name) {
        lenient().when(job.getName()).thenReturn(name);

        // The name of the job also defines its URL, that's why the stub for getUrl() is defined here as well.
        // You could argue, that 'withUrl' could be a separate method on the builder,
        // but then this would allow for creation of impossible scenarios, such as:
        // job.withName("a-name").withUrl("something completely different"), which leads nowhere.
        return withShortUrl(name);
    }

    private JobStateRecipe withShortUrl(String url) {
        lenient().when(job.getShortUrl()).thenReturn(url);

        return this;
    }

    public JobStateRecipe withDisplayName(String name) {
        lenient().when(job.getDisplayNameOrNull()).thenReturn(name);
        lenient().when(job.getDisplayName()).thenReturn(name);

        return this;
    }

    public JobStateRecipe thatHasNeverRun() {
        buildHistory.clear();

        return this;
    }

    public JobStateRecipe thatIsNotBuildable() {
        when(job.isBuildable()).thenReturn(Boolean.FALSE);

        return this;
    }

    public JobStateRecipe thatIsAnExternalJob() {
        job = mock(ViewJob.class);

        when(job.isBuildable()).thenReturn(Boolean.FALSE);

        return this;
    }

    public JobStateRecipe whereTheLast(BuildStateRecipe recipe) {
        return updatedWithOnlyOneHistoryEntryFor(recipe.get());
    }

    public JobStateRecipe andThePrevious(BuildStateRecipe recipe) {
        return updatedWithEarliestHistoryEntryFor(recipe.get());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Job<?, ?> get() {
        AbstractBuild earlierBuild, earliestBuild;

        // link "previous" builds ...
        while (buildHistory.size() > 1) {
            earliestBuild = buildHistory.pop();
            earlierBuild  = buildHistory.peek();

            lenient().when(earlierBuild.getPreviousBuild()).thenReturn(earliestBuild);
        }

        // pick the first build from the build history and make it the "last build"
        if (buildHistory.size() == 1) {
            lenient().doReturn(buildHistory.pop()).when(job).getLastBuild();
        }
        
        // mock the necessary methods to get the currentBuilds
        // it will return the full list so make sure it contains only building builds
        lenient().doReturn(runList).when(job).getNewBuilds();
        lenient().doReturn(runList).when(runList).filter(any(Predicate.class));
        lenient().doReturn(allBuilds.iterator()).when(runList).iterator();

        return job;
    }

    private JobStateRecipe updatedWithEarliestHistoryEntryFor(AbstractBuild build) {
        buildHistory.push(build);
        allBuilds.add(build);

        return this;
    }

    private JobStateRecipe updatedWithOnlyOneHistoryEntryFor(AbstractBuild build) {
        buildHistory.clear();
        allBuilds.clear();

        return updatedWithEarliestHistoryEntryFor(build);
    }
}
