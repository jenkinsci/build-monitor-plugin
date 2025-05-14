package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FolderUtils.createFolder;
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
class ShouldSupportCloudBeesFoldersTest {

    @Test
    void test(Page p, JenkinsRule j) {
        createFolder(j, "Search Services")
                .addJobs(
                        createFreeStyleProject(j, "Third Party System").get(),
                        createFolder(j, "Contracts")
                                .addJobs(createFreeStyleProject(j, "Librarian").get())
                                .get());
        var view =
                createBuildMonitorView(j, "Build Monitor").displayAllProjects().displayNestedProjectsFromSubfolders();

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJob("Search Services » Librarian")
                .hasJob("Search Services » Contracts » Third Party System");
    }
}
