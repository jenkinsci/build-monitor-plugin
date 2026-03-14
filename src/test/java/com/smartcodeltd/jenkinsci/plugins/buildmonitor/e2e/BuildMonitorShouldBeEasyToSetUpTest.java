package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class BuildMonitorShouldBeEasyToSetUpTest {

    @Test
    void test(Page p, JenkinsRule j) {
        var project = createFreeStyleProject(j, "Example job").get();
        var view = createBuildMonitorView(j, "Build Monitor").addJobs(project);

        BuildMonitorViewPage.from(p, view).goTo().hasJobsCount(1).hasJob(project.getDisplayName());
    }
}
