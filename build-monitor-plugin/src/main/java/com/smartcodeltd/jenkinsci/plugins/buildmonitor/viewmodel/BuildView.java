package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline.WorkflowNodeTraversal;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.Duration;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.DurationInMilliseconds;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.duration.HumanReadableDuration;
import hudson.model.*;
import hudson.scm.ChangeLogSet;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.functions.NullSafety.getOrElse;

public class BuildView implements BuildViewModel {

    private final Run<?,?> build;
    private final boolean isPipeline;
    private final RelativeLocation parentJobLocation;
    private final Date systemTime;

    @VisibleForTesting
    static BuildView of(Run<?, ?> build) {
        return new BuildView(build, false, RelativeLocation.of(build.getParent()), new Date());
    }

    public static BuildView of(Run<?, ?> build, boolean isPipeline, RelativeLocation parentJobLocation, Date systemTime) {
        return new BuildView(build, isPipeline, parentJobLocation, systemTime);
    }

    @Override
    public String name() {
        return build.getDisplayName();
    }

    // todo: fix the double slash added when there's no parent
    @Override
    public String url() {
        return parentJobLocation.url() + "/" + build.getNumber() + "/";
    }

    @Override
    public Result result() {
        return build.getResult();
    }

    @Override
    public boolean isRunning() {
        return isRunning(this.build);
    }

    private boolean isRunning(Run<?, ?> build) {
        return build.hasntStartedYet() || build.isBuilding() || build.isLogUpdated();
    }

    @Override
    public Duration elapsedTime() {
        return new HumanReadableDuration(now() - whenTheBuildStarted());
    }

    @Override
    public Duration timeElapsedSince() {
        return new DurationInMilliseconds(now() - (whenTheBuildStarted() + build.getDuration()));
    }

    @Override
    public Duration duration() {
        return new HumanReadableDuration(build.getDuration());
    }

    @Override
    public Duration estimatedDuration() {
        return new HumanReadableDuration(build.getEstimatedDuration());
    }

    @Override
    public int progress() {
        if (! isRunning()) {
            return 0;
        }

        if (isTakingLongerThanUsual()) {
            return 100;
        }

        long elapsedTime       = now() - whenTheBuildStarted(),
             estimatedDuration = build.getEstimatedDuration();

        if (estimatedDuration > 0) {
            return (int) ((float) elapsedTime / (float) estimatedDuration * 100);
        }

        return 100;
    }

    @Override
    public String description() {
        return getOrElse(build.getDescription(), "");
    }

    @Override
    public boolean isPipeline() {
        return isPipeline;
    }

    @Override
    public List<String> pipelineStages() {
        WorkflowRun currentBuild = (WorkflowRun) this.build;
        FlowExecution execution = currentBuild.getExecution();
        if (execution != null) {
            WorkflowNodeTraversal traversal = new WorkflowNodeTraversal();
            traversal.start(execution.getCurrentHeads());
            return traversal.getStages();
        }

        return Collections.emptyList();
    }

    private boolean isTakingLongerThanUsual() {
        return elapsedTime().greaterThan(estimatedDuration());
    }

    @Override
    public boolean hasPreviousBuild() {
        return null != build.getPreviousBuild();
    }

    @Override
    public BuildViewModel previousBuild() {
        return new BuildView(build.getPreviousBuild(), isPipeline, this.parentJobLocation, systemTime);
    }

    @Override
    public Set<String> culprits() {
        return getUsers(new Reader() {
            @Override
            public Iterable<String> readUsersFrom(AbstractBuild<?, ?> jenkinsBuild) {
                return transform(jenkinsBuild.getCulprits(), new Function<User, String>() {
                    @Override
                    public String apply(User culprit) {
                        return culprit.getFullName();
                    }
                });
            }
        });
    }

    @Override
    public Set<String> committers() {
        return getUsers(new Reader() {
            @Override
            public Iterable<String> readUsersFrom(AbstractBuild<?, ?> jenkinsBuild) {
                return transform(nonNullIterable(jenkinsBuild.getChangeSet()), new Function<ChangeLogSet.Entry, String>() {
                    @Override
                    public String apply(ChangeLogSet.Entry entry) {
                        return entry.getAuthor().getFullName();
                    }
                });
            }

            private <T> T nonNullIterable(T list) {
                return (T) getOrElse(list, newArrayList());
            }
        });
    }

    @Override
    public <A extends Action> Optional<A> detailsOf(Class<A> jenkinsAction) {
        return Optional.fromNullable(build.getAction(jenkinsAction));
    }
    
    @Override
	public <A extends Action> List<A> allDetailsOf(Class<A> jenkinsAction) {
    	return build.getActions(jenkinsAction);
	}

    @Override
    public String toString() {
        return name();
    }


    private long now() {
        return systemTime.getTime();
    }

    private long whenTheBuildStarted() {
        return build.getTimestamp().getTimeInMillis();
    }


    private BuildView(Run<?, ?> build, boolean isPipeline, RelativeLocation parentJobLocation, Date systemTime) {
        this.build = build;
        this.isPipeline = isPipeline;
        this.parentJobLocation = parentJobLocation;
        this.systemTime = systemTime;
    }

    private interface Reader {
        Iterable<String> readUsersFrom(AbstractBuild<?, ?> jenkinsBuild);
    }

    private Set<String> getUsers(Reader reader) {
        Set<String> users = new TreeSet<String>();

        if (build instanceof AbstractBuild<?, ?>) {
            AbstractBuild<?, ?> jenkinsBuild = (AbstractBuild<?, ?>) build;

            Iterables.addAll(users, reader.readUsersFrom(jenkinsBuild));
        }

        return users;
    }
}