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

    private StaticJenkinsAPIs staticJenkinsAPIs;

    public static BuildCulpritsRetriever getInstanceForRun(Run<?, ?> run, StaticJenkinsAPIs staticJenkinsAPIs) {
        BuildCulpritsRetriever retriever;
        if (PipelineHelper.isWorkflowRun(run, staticJenkinsAPIs)) {
            retriever = new BuildCulpritsWorkflowRun();
        } else if (run instanceof AbstractBuild) {
            retriever = new BuildCulpritsAbstractBuild();
        } else {
            retriever = new BuildCulpritsNotImplemented();
        }
        retriever.staticJenkinsAPIs = staticJenkinsAPIs;
        return retriever;
    }

    public abstract Set<String> getCulprits(Run<?, ?> run);

    protected abstract Set<String> getCommittersForRun(Run<?, ?> run);

    public Set<String> getCommitters(Run<?, ?> run) {
        Set<String> committers = getCommittersForRun(run);
        // If no committers were found, recursively get upstream committers:
        if (committers.isEmpty()) {
            Cause.UpstreamCause upstreamCause = run.getCause(Cause.UpstreamCause.class);
            if (upstreamCause != null) {
                Run<?, ?> upstreamRun = upstreamCause.getUpstreamRun();
                if (upstreamRun != null) {
                    // Use the correct retriever for the upstream run type — it may differ from this one
                    // (e.g. a Pipeline triggered by a FreeStyleProject).
                    BuildCulpritsRetriever upstreamRetriever = getInstanceForRun(upstreamRun, staticJenkinsAPIs);
                    committers.addAll(upstreamRetriever.getCommitters(upstreamRun));
                }
            }
        }
        return committers;
    }
}
