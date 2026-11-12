package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Result;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldSupportDisablingFadeInTest {

    private static final int JOB_COUNT = 3;

    @Test
    void fadesInJobsByDefault(Page p, JenkinsRule j) {
        createJobs(j);
        var view = createBuildMonitorView(j, "Build Monitor").displayAllProjects();

        BuildMonitorViewPage.from(p, view).goTo().hasJobsCount(JOB_COUNT).fadesInJobs();
    }

    @Test
    void showsJobsWithoutFadingThemInWhenFadeInIsDisabled(Page p, JenkinsRule j) {
        createJobs(j);
        var view =
                createBuildMonitorView(j, "Build Monitor").displayAllProjects().withoutFadeIn();

        BuildMonitorViewPage.from(p, view).goTo().hasJobsCount(JOB_COUNT).doesNotFadeInJobs();
    }

    private void createJobs(JenkinsRule j) {
        IntStream.rangeClosed(1, JOB_COUNT)
                .forEach(i -> createFreeStyleProject(j, "job-" + i).run(Result.SUCCESS));
    }
}
