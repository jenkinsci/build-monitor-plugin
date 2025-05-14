package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.addProjectToView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.TestUtils.createFreeStyleProject;

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
public class ProjectStatusShouldBeEasyToDetermineTest {

    @Test
    void visualisingASuccessfulProject(Page p, JenkinsRule j) {
        var project = createFreeStyleProject(j, "Successful");
        project.scheduleBuild2(0);
        try {
            j.assertBuildStatus(Result.SUCCESS, project.scheduleBuild2(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var view = createBuildMonitorView(j, "Build Monitor");
        addProjectToView(project, view);

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJobsCount(1)
                .getJob(project.getDisplayName())
                .hasStatus(Result.SUCCESS);
    }

    @Test
    void visualisingAFailingProject(Page p, JenkinsRule j) {
        var project = createFreeStyleProject(j, "Failing");
        project.getBuildersList().add(new hudson.tasks.Shell("exit 1"));
        try {
            j.assertBuildStatus(Result.FAILURE, project.scheduleBuild2(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var view = createBuildMonitorView(j, "Build Monitor");
        addProjectToView(project, view);

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJobsCount(1)
                .getJob(project.getDisplayName())
                .hasStatus(Result.FAILURE);
    }
}
