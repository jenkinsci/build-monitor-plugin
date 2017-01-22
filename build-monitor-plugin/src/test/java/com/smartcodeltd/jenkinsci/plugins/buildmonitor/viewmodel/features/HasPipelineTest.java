package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HasPipelineTest {
    private JobView job;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void one_running_stage_is_shown() throws Exception {
        job = a(jobView().which(isPipelineCurrentlyRunning("Compile")));

        assertThat(pipelineOf(job).currentStages(), is("[Compile]"));
    }

    @Test
    public void several_running_stages_are_shown() throws Exception {
        job = a(jobView().which(isPipelineCurrentlyRunning("Test", "Deploy")));

        assertThat(pipelineOf(job).currentStages(), is("[Test, Deploy]"));
    }

    @Test
    public void no_running_stages_should_return_null() throws Exception {
        job = a(jobView().which(isPipelineNotRunning()));

        assertThat(pipelineOf(job), is(nullValue()));
    }

    private HasPipeline isPipelineNotRunning() {
        return new HasPipeline(new TestPipelineFetcher(Collections.<String>emptyList()));
    }

    private HasPipeline isPipelineCurrentlyRunning(String... stages) {
        return new HasPipeline(new TestPipelineFetcher(newArrayList(stages)));
    }

    private HasPipeline.Pipeline pipelineOf(JobView job) {
        return job.which(HasPipeline.class).asJson();
    }

    private static class TestPipelineFetcher implements HasPipeline.PipelineFetcher {
        private final List<String> stages;

        TestPipelineFetcher(List<String> stages) {
            this.stages = stages;
        }

        @Override
        public List<String> fetchStagesFrom(JobView jobView) {
            return stages;
        }
    }
}