package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.ExternalJobUtils;
import hudson.model.Result;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldSupportExternalProjectsTest {

    @Test
    void test(Page p, JenkinsRule j) {
        var job = ExternalJobUtils.createExternalJob(j, "External job")
                .notifyOf(Result.SUCCESS)
                .get();
        var view = createBuildMonitorView(j, "Build Monitor").addJobs(job);

        BuildMonitorViewPage.from(p, view).goTo().getJob(job.getDisplayName()).hasStatus(Result.SUCCESS);
    }
}
