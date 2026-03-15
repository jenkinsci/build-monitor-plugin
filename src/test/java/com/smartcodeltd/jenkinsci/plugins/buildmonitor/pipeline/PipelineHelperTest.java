package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import hudson.Plugin;
import hudson.model.AbstractBuild;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class PipelineHelperTest {

    @Test
    void isWorkflowRun() {
        try (MockedStatic<Jenkins> mockedJenkins = mockStatic(Jenkins.class)) {
            Jenkins jenkins = createMockJenkins(mockedJenkins);
            Plugin mockedPipeline = mock(Plugin.class);
            WorkflowRun workflowRun = mock(WorkflowRun.class);
            when(jenkins.getPlugin("workflow-job")).thenReturn(mockedPipeline);

            assertTrue(PipelineHelper.isWorkflowRun(workflowRun, new StaticJenkinsAPIs()));
        }
    }

    @Test
    void isNotWorkflowRun() {
        try (MockedStatic<Jenkins> mockedJenkins = mockStatic(Jenkins.class)) {
            createMockJenkins(mockedJenkins);
            AbstractBuild abstractBuild = mock(AbstractBuild.class);

            assertFalse(PipelineHelper.isWorkflowRun(abstractBuild, new StaticJenkinsAPIs()));
        }
    }

    private Jenkins createMockJenkins(MockedStatic<Jenkins> mockedJenkins) {
        Jenkins jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::get).thenReturn(jenkins);
        return jenkins;
    }
}
