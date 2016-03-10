package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.BuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.recipes.With;
import org.junit.Ignore;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.prerequisites.FolderExists.aFolder;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.prerequisites.FreestyleProjectExists.aFreestyleProject;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.CreateBuildMonitorView.createABuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.ConfigureJobFilters.includesAllTheJobs;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.ConfigureJobFilters.recursesInSubFolders;
import static org.hamcrest.MatcherAssert.assertThat;

// @TODO: Work in progress
@Ignore("There's a problem with BrowserStack client running on CloudBees Jenkins")
public class BehaviourWithCloudbeesFoldersTest extends AcceptanceTest {

    private BuildMonitor buildMonitor;

    @Test
    @With(plugins = {
            "cloudbees-folder-4.2.3.hpi",
            "buildgraph-view-1.0.hpi", "git-1.5.0.hpi", "git-client-1.8.0.jpi"
    })
    public void displays_jobs_nested_in_folders() throws Exception {

        given.IHave(
            aFreestyleProject("Deploy to QA"),
            aFolder("Librarian Project").containing(
                aFreestyleProject("Librarian Core"),
                aFolder("Micro-services").containing(
                    aFreestyleProject("Indexer")
                )
            )
        ).

        WhenI(createABuildMonitorView("Search Services").that(
            includesAllTheJobs(),
            recursesInSubFolders()
        ));

        // then ...

        buildMonitor = buildMonitorView("Search Services");

        assertThat(buildMonitor.job("deploy-to-qa"), isDisplayed());
        assertThat(buildMonitor.job("librarian-project-micro-services-indexer"), isDisplayed());
        assertThat(buildMonitor.job("librarian-project-librarian-core"), isDisplayed());
    }
}
