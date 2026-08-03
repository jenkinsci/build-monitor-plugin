package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.PipelineJobUtils.createPipelineJob;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldDisplayConcurrentBuildsTest {

    @Test
    void test(Page p, JenkinsRule j) {
        createPipelineJob(j, "My Pipeline", "pipelineStage.jenkinsfile").run().run();
        var view = createBuildMonitorView(j, "Build Monitor").displayAllProjects();

        BuildMonitorViewPage.from(p, view).goTo().getJob("My Pipeline").hasBuilds("#2", "#1");
    }
}
