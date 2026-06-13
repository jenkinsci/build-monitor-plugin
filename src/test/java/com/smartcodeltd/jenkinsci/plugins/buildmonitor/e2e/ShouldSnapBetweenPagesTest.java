package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldSnapBetweenPagesTest {

    @Test
    void snapsToTheSecondPageWhenTheViewportIsScrolledPastTheBoundary(Page p, JenkinsRule j) {
        FreeStyleProject[] projects = IntStream.rangeClosed(1, 5)
                .mapToObj(index -> createFreeStyleProject(j, "Job " + index)
                        .run(Result.SUCCESS)
                        .getProject())
                .toArray(FreeStyleProject[]::new);

        var view = createBuildMonitorView(j, "Build Monitor")
                .addJobs(projects)
                .withMaximumColumns(2)
                .withTextScale(2.0);

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJobsCount(5)
                .hasPageCount(3)
                .scrollPastPageBoundaryTowards(2)
                .hasSnappedToPage(2)
                .hasActivePage(2);
    }
}
