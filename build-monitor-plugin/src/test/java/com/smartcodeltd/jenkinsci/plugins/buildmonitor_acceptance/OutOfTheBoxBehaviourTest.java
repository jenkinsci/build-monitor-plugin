package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.BuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.recipes.With;
import org.junit.Ignore;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.prerequisites.FreestyleProjectExists.aFreestyleProject;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.ConfigureJobFilters.includesAllTheJobs;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.CreateBuildMonitorView.createABuildMonitorView;
import static org.junit.Assert.assertThat;

// @TODO: Work in progress
@Ignore("There's a problem with BrowserStack client running on CloudBees Jenkins")
public class OutOfTheBoxBehaviourTest extends AcceptanceTest {

    private BuildMonitor buildMonitor;

    @Test
    @With(plugins = { "buildgraph-view-1.0.hpi", "git-1.5.0.hpi", "git-client-1.8.0.jpi" })
    public void correctly_displays_successful_and_failing_jobs() throws Exception {

        given.IHave(
            aFreestyleProject("example-build").configuredToRun(aPassingShellScript()).executed(),
            aFreestyleProject("example-acceptance").configuredToRun(aFailingShellScript()).executed()
        ).

        WhenI(createABuildMonitorView("Build Monitor").that(includesAllTheJobs()));

        // then ...

        buildMonitor = buildMonitorView("Build Monitor");

        assertThat(buildMonitor.job("example-build"), isSuccessful());
        assertThat(buildMonitor.job("example-acceptance"), isFailing());
    }
}