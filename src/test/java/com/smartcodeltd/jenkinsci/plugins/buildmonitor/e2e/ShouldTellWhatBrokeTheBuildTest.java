package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FailureCauseManagementUtils.createFailureCause;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Result;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldTellWhatBrokeTheBuildTest {

    @Test
    void test(Page p, JenkinsRule j) {
        createFailureCause(j, "Rogue AI", "Pod bay doors didn't open");
        createFreeStyleProject(j, "Discovery One")
                .addTask(new hudson.tasks.Shell("exit 1"))
                .run(Result.FAILURE)
                .getProject();
        var view = createBuildMonitorView(j, "Build Monitor").displayAllProjects();

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .getJob("Discovery One")
                .hasIdentifiedProblem("Identified problem: Rogue AI");
    }
}
