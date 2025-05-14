package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.addProjectToView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.TestUtils.createAndRunJob;

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
public class ShouldDisplayBadgesTest {

    // TODO - add tests for showing/hiding badges

    @Test
    void test(Page p, JenkinsRule j) {
        var run = createAndRunJob(j, "Lemonworld CI", "singleStagePipeline.jenkinsfile", Result.SUCCESS);

        var view = createBuildMonitorView(j, "Build Monitor");
        addProjectToView(run.getParent(), view);

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .getJob("Lemonworld CI")
                .hasBadge("Example badge");
    }
}
