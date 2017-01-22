package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class HasPipeline implements Feature {

    private JobView job;

    private final PipelineFetcher pipelineFetcher;

    public HasPipeline() {
        this(new DefaultPipelineFetcher());
    }

    @VisibleForTesting
    HasPipeline(PipelineFetcher pipelineFetcher) {
        this.pipelineFetcher = pipelineFetcher;
    }

    @Override
    public Feature of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Pipeline asJson() {
        List<String> stages = pipelineFetcher.fetchStagesFrom(job);

        return !stages.isEmpty()
                ? new Pipeline(stages)
                : null;
    }

    public static class Pipeline {

        private final List<String> executingStages;

        public Pipeline(List<String> executingStages) {
            this.executingStages = executingStages;
        }

        @JsonProperty
        public String currentStages() {
            return "[" + Joiner.on(", ").join(executingStages) + "]";
        }

    }

    interface PipelineFetcher {
        List<String> fetchStagesFrom(JobView jobView);
    }

    private static class DefaultPipelineFetcher implements PipelineFetcher {

        @Override
        public List<String> fetchStagesFrom(JobView jobView) {
            return jobView.lastBuild().stages();
        }
    }
}
