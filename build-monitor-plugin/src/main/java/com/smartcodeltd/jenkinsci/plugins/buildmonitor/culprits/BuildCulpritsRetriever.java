package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.User;
import hudson.scm.ChangeLogSet;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.functions.NullSafety.getOrElse;

/**
 * Created by Erik Håkansson on 2017-04-25.
 * https://github.com/erikhakansson
 */
public class BuildCulpritsRetriever {

  private static final String Pipeline = "workflow-aggregator";
  private final StaticJenkinsAPIs staticJenkinsAPIs;

  public BuildCulpritsRetriever(StaticJenkinsAPIs staticJenkinsAPIs) {
    this.staticJenkinsAPIs = staticJenkinsAPIs;
  }

  public Set<String> getCulprits(Run<?, ?> build) {
    if (staticJenkinsAPIs.hasPlugin(Pipeline) && build instanceof WorkflowRun) {
      return getCulpritsForWorkflowRun((WorkflowRun) build);
    } else if (build instanceof AbstractBuild) {
      return getCulpritsForAbstractBuild((AbstractBuild<?, ?>) build);
    } else {
      //Not implemented
      return Collections.emptySet();
    }
  }

  public Set<String> getCommitters(Run<?, ?> build) {
    Set<String> committers;
    if (staticJenkinsAPIs.hasPlugin(Pipeline) && build instanceof WorkflowRun) {
      committers = getCommittersForWorkflowRun((WorkflowRun) build);
    } else if (build instanceof AbstractBuild<?, ?>) {
      committers = getCommittersForAbstractBuild((AbstractBuild<?, ?>) build);
    } else {
      //not implemented:
      committers = Collections.emptySet();
    }
    //If no committers were found, recursively get upstream committers:
    if (committers.isEmpty()) {
      Cause.UpstreamCause upstreamCause = build.getCause(Cause.UpstreamCause.class);
      if (upstreamCause != null) {
        Run<?, ?> upstreamRun = upstreamCause.getUpstreamRun();
        if (upstreamRun != null) {
          committers.addAll(getCommitters(upstreamRun));
        }
      }
    }
    return committers;
  }

  private Set<String> getCulpritsForAbstractBuild(AbstractBuild<?, ?> build) {
    Set<String> culprits = new TreeSet<String>();
    Iterable<String> forAbstractBuild = transform(build.getCulprits(), new Function<User, String>() {
      @Override
      public String apply(User culprit) {
        return culprit.getFullName();
      }
    });
    Iterables.addAll(culprits, forAbstractBuild);
    return culprits;
  }

  private Set<String> getCulpritsForWorkflowRun(WorkflowRun build) {
    Set<String> culprits = new TreeSet<String>();
    //Workaround while waiting for https://issues.jenkins-ci.org/browse/JENKINS-24141
    WorkflowRun previous = build.getPreviousCompletedBuild();
    if (build.isBuilding()) {
      //We are currently building so add culprits from previous build (if any)
      if (previous != null && previous.getResult().isWorseThan(Result.SUCCESS)) {
        culprits.addAll(getCommitters(previous));
      }
    }
    culprits.addAll(getCommitters(build));

    //Get culprits from earlier builds
    if (previous != null && previous.getPreviousNotFailedBuild() != null) {
      culprits.addAll(getCulpritsForWorkflowRuns(previous.getPreviousNotFailedBuild(), previous));
    }
    return culprits;
  }

  private Set<String> getCommittersForAbstractBuild(AbstractBuild<?, ?> build) {
    Set<String> committers = new TreeSet<String>();
    Iterable<String> iterable = transform(nonNullIterable(((AbstractBuild<?, ?>) build).getChangeSet()), new Function<ChangeLogSet.Entry, String>() {
      @Override
      public String apply(ChangeLogSet.Entry entry) {
        return entry.getAuthor().getFullName();
      }
    });
    Iterables.addAll(committers, iterable);
    return committers;
  }

  private Set<String> getCommittersForWorkflowRun(WorkflowRun build) {
    Set<String> committers = new TreeSet<String>();
    for (ChangeLogSet<? extends ChangeLogSet.Entry> changeLogSet : ((WorkflowRun) build).getChangeSets()) {
      Iterables.addAll(committers, transform(nonNullIterable(changeLogSet), new Function<ChangeLogSet.Entry, String>() {
        @Override
        public String apply(@Nullable ChangeLogSet.Entry entry) {
          return entry != null ? entry.getAuthor().getFullName() : null;
        }
      }));
    }
    return committers;
  }

  private Set<String> getCulpritsForWorkflowRuns(WorkflowRun from, WorkflowRun to) {
    Set<String> culprits = new TreeSet<String>();
    WorkflowRun next = null;
    while (true) {
      next = next == null ? from.getNextBuild() : next.getNextBuild();
      if (next == null || next.getNumber() >= to.getNumber()) {
        break;
      }
      Iterables.addAll(culprits, getCommitters(next));
    }
    return culprits;
  }

  @SuppressWarnings("unchecked")
  private static <T> T nonNullIterable(T list) {
    return (T) getOrElse(list, newArrayList());
  }
}
