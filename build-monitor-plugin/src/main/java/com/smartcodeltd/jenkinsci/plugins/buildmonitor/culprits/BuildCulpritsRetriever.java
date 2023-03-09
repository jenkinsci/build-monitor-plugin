package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline.PipelineHelper;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Run;

import java.util.Set;

/**
 * Created by <a href="https://github.com/erikhakansson">Erik Håkansson</a> on 2017-04-25.
 */
public abstract class BuildCulpritsRetriever {

    public static BuildCulpritsRetriever getInstanceForRun(Run<?, ?> run, StaticJenkinsAPIs staticJenkinsAPIs) {
        if (PipelineHelper.isWorkflowRun(run, staticJenkinsAPIs)) {
            return new BuildCulpritsWorkflowRun();
        } else if (run instanceof AbstractBuild) {
            return new BuildCulpritsAbstractBuild();
        } else {
            return new BuildCulpritsNotImplemented();
        }
    }

    public abstract Set<String> getCulprits(Run<?, ?> run);

    protected abstract Set<String> getCommittersForRun(Run<?, ?> run);

    public Set<String> getCommitters(Run<?, ?> run) {
        Set<String> committers = getCommittersForRun(run);
        //If no committers were found, recursively get upstream committers:
        if (committers.isEmpty()) {
            Cause.UpstreamCause upstreamCause = run.getCause(Cause.UpstreamCause.class);
            if (upstreamCause != null) {
                Run<?, ?> upstreamRun = upstreamCause.getUpstreamRun();
                if (upstreamRun != null) {
                    committers.addAll(getCommitters(upstreamRun));
                }
            }
        }
        return committers;
    }
}
