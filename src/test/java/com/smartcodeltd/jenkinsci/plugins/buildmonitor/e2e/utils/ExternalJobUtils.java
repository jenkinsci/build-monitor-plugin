package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import hudson.cli.CLI;
import hudson.model.ExternalJob;
import hudson.model.Result;
import java.util.ArrayList;
import java.util.List;
import org.jvnet.hudson.test.JenkinsRule;

public class ExternalJobUtils {

    public static FluentExternalJob createExternalJob(JenkinsRule jenkins, String jobName) {
        try {
            ExternalJob job = jenkins.createProject(ExternalJob.class, jobName);

            return new FluentExternalJob(jenkins, job);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class FluentExternalJob {

        private final JenkinsRule jenkins;

        private final ExternalJob job;

        public FluentExternalJob(JenkinsRule jenkins, ExternalJob job) {
            this.jenkins = jenkins;
            this.job = job;
        }

        public FluentExternalJob notifyOf(Result result) {
            setExternalBuildResult(job.getDisplayName(), result == Result.SUCCESS ? "0" : "1");
            return this;
        }

        public void setExternalBuildResult(String projectName, String result) {
            executeCommand(
                    "set-external-build-result",
                    "--job",
                    projectName,
                    "--result",
                    result,
                    "--log",
                    String.format("%s finished with %s", projectName, result));
        }

        private int executeCommand(String... args) {
            String jenkinsUrl = jenkins.getInstance().getRootUrl();
            try {
                List<String> cliArgs = new ArrayList<>(List.of("-s", jenkinsUrl, "-http"));
                cliArgs.addAll(List.of(args));

                return CLI._main(cliArgs.toArray(new String[0]));
            } catch (Exception e) {
                throw new RuntimeException(String.format("Couldn't connect to Jenkins at '%s'", jenkinsUrl), e);
            }
        }

        public ExternalJob get() {
            return job;
        }
    }
}
