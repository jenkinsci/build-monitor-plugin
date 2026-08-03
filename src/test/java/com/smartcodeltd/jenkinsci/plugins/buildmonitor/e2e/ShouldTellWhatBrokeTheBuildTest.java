package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.FailureCauseManagementPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldTellWhatBrokeTheBuildTest {

    @Test
    void test(Page p, JenkinsRule j) {
        var project = createFreeStyleProject(j, "Discovery One")
                .addTask(new hudson.tasks.Shell("exit 1"))
                .get();

        FailureCauseManagementPage.from(p, project)
                .goTo()
                .createFailureCause(
                        "Rogue AI", "Pod bay doors didn't open", "Build step 'Execute shell' marked build as failure");

        project.scheduleBuild2(0);
        var view = createBuildMonitorView(j, "Build Monitor").displayAllProjects();
        BuildMonitorViewPage.from(p, view).goTo().getJob("Discovery One").hasIdentifiedProblem("Rogue AI");
    }
}
