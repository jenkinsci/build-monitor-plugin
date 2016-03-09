package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.recipes.With;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.prerequisites.FreestyleProjectExists.aFreestyleProject;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.CreateBuildMonitorView.createABuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.ConfigureJobFilters.includesAllTheJobs;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks.DefineBuildFailureCause.defineABuildFailureCause;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// @TODO: Work in progress
public class BehaviourWithBuildFailureAnalyserTest extends AcceptanceTest {

    @Test
    @With(plugins = { "buildgraph-view-1.0.hpi", "git-1.5.0.hpi", "git-client-1.8.0.jpi" })
    public void displays_potential_failure_cause_when_it_is_known() throws Exception {

        given.I(
            defineABuildFailureCause(
                "Shell script failure",
                "A shell script failure happened",
                "Build step 'Execute shell' marked build as failure"
            ),
            createABuildMonitorView("Build Monitor").that(includesAllTheJobs())
        ).

        WhenIHave(aFreestyleProject("example-acceptance").configuredToRun(aFailingShellScript()).executed());

        // then ...

        assertThat(buildMonitorView("Build Monitor").job("example-acceptance").possibleFailureCause(), is("Identified problem: Shell script failure"));
    }
}