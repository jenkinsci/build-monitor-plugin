package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import com.google.common.io.Resources;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jvnet.hudson.test.JenkinsRule;

public class TestUtils {

    public static FreeStyleProject createFreeStyleProject(JenkinsRule jenkins, String name) {
        try {
            return jenkins.createFreeStyleProject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static WorkflowRun createAndRunJob(
            JenkinsRule jenkins, String jobName, String jenkinsFileName, Result expectedResult) {
        WorkflowJob job;
        try {
            job = TestUtils.createJob(jenkins, jobName, jenkinsFileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            jenkins.assertBuildStatus(expectedResult, job.scheduleBuild2(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return job.getLastBuild();
    }

    public static WorkflowJob createJob(JenkinsRule jenkins, String jobName, String jenkinsFileName) throws Exception {
        WorkflowJob job = jenkins.createProject(WorkflowJob.class, jobName);

        URL resource = Resources.getResource(TestUtils.class, jenkinsFileName);
        String jenkinsFile = Resources.toString(resource, StandardCharsets.UTF_8);
        job.setDefinition(new CpsFlowDefinition(jenkinsFile, true));
        return job;
    }
}
