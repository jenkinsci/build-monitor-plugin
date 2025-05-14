package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import com.google.common.io.Resources;
import hudson.model.Result;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jvnet.hudson.test.JenkinsRule;

public class PipelineJobUtils {

    public static FluentPipelineJob createPipelineJob(JenkinsRule jenkins, String jobName, String jenkinsFileName) {
        try {
            WorkflowJob job = jenkins.createProject(WorkflowJob.class, jobName);

            URL resource = Resources.getResource(PipelineJobUtils.class, jenkinsFileName);
            String jenkinsFile = Resources.toString(resource, StandardCharsets.UTF_8);
            job.setDefinition(new CpsFlowDefinition(jenkinsFile, true));

            return new FluentPipelineJob(jenkins, job);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class FluentPipelineJob {

        private final JenkinsRule jenkins;

        private final WorkflowJob job;

        public FluentPipelineJob(JenkinsRule jenkins, WorkflowJob job) {
            this.jenkins = jenkins;
            this.job = job;
        }

        public WorkflowRun run(Result expectedResult) {
            try {
                WorkflowRun build = job.scheduleBuild2(0).get();
                jenkins.assertBuildStatus(expectedResult, build);
                return build;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public WorkflowJob get() {
            return job;
        }
    }
}
