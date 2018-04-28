package com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import hudson.Plugin;
import hudson.model.AbstractBuild;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, WorkflowRun.class})
public class PipelineHelperTest {

    @Mock
    private Jenkins jenkins;

    @Mock
    private Plugin mockedPipeline;

    private WorkflowRun workflowRun;

    @Mock
    private AbstractBuild abstractBuild;

    @Before
    public void setup() {
        PowerMockito.mockStatic(Jenkins.class);
        workflowRun = PowerMockito.mock(WorkflowRun.class);
        PowerMockito.when(Jenkins.getInstance()).thenReturn(jenkins);
        PowerMockito.when(jenkins.getPlugin("workflow-aggregator")).thenReturn(mockedPipeline);
    }

    @Test
    public void isWorkflowRun() {
        Assert.assertTrue(PipelineHelper.isWorkflowRun(workflowRun, new StaticJenkinsAPIs()));
    }

    @Test
    public void isNotWorkflowRun() {
        Assert.assertFalse(PipelineHelper.isWorkflowRun(abstractBuild, new StaticJenkinsAPIs()));
    }

}