package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Result;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldWrapLongJobNamesTest {

    // too long for one line, differing only in the separator they use
    private static final List<String> NAMES = List.of(
            "MyOrganization-MyTeam-MyApplication-MyJob-with-long-name",
            "MyOrganization.MyTeam.MyApplication.MyJob-with-long-name",
            "MyOrganization_MyTeam_MyApplication_MyJob-with-long-name",
            "MyOrganization MyTeam MyApplication MyJob with long name",
            "MyOrganizationMyTeamMyApplicationMyJobWithLongName");

    @Test
    void test(Page p, JenkinsRule j) {
        NAMES.forEach(name -> createFreeStyleProject(j, name).run(Result.SUCCESS));
        var view = createBuildMonitorView(j, "Build Monitor").displayAllProjects();

        var monitor = BuildMonitorViewPage.from(p, view).goTo();

        NAMES.forEach(
                name -> monitor.getJob(name).hasNameWrappedOntoMultipleLines().hasNameContainedWithinCell());
    }
}
