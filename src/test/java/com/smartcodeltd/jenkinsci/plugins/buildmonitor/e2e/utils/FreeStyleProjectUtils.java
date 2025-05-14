package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.Builder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jvnet.hudson.test.JenkinsRule;

public class FreeStyleProjectUtils {

    public static FluentFreeStyleProject createFreeStyleProject(JenkinsRule jenkins, String name) {
        try {
            return new FluentFreeStyleProject(jenkins, jenkins.createFreeStyleProject(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class FluentFreeStyleProject {

        private final JenkinsRule jenkins;

        private final FreeStyleProject project;

        private final List<Builder> builders = new ArrayList<>();

        public FluentFreeStyleProject(JenkinsRule jenkins, FreeStyleProject project) {
            this.jenkins = jenkins;
            this.project = project;
        }

        public FluentFreeStyleProject addTask(Builder builder) {
            builders.add(builder);
            return this;
        }

        public FreeStyleBuild run(Result expectedResult) {
            try {
                project.getBuildersList().addAll(builders);
                FreeStyleBuild build = project.scheduleBuild2(0).get();
                jenkins.assertBuildStatus(expectedResult, build);
                return build;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public FreeStyleProject get() {
            return project;
        }
    }
}
