package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
public class ShouldDisplayTestProgressTest {

    @Test
    void test() {
        //        givenThat(richard)
        //                .wasAbleTo(
        //                        Navigate.to(jenkins.url()),
        //                        HaveAPipelineProjectCreated.called("My Pipeline")
        //                                .andConfiguredTo(
        //                                        SetPipelineDefinition.asFollows(
        //                                                GroovyScriptThat.Pause_In_Middle_Of_Tests.code()),
        //                                        Disable.executingConcurrentBuilds()),
        //                        ScheduleABuild.of("My Pipeline"),
        //                        Navigate.to(jenkins.url()),
        //                        ScheduleABuild.of("My Pipeline"));
        //
        //        when(richard)
        //                .attemptsTo(CreateABuildMonitorView.called("Build Monitor")
        //                        .andConfigureItTo(
        //                                DisplayAllProjects.usingARegularExpression(),
        // DisplayJunitRealtimeProgress.bars()));
        //
        //        then(richard)
        //                .should(seeThat(
        //                        ProjectWidget.of("My Pipeline").testProgressBars(),
        //                        WebElementStateMatchers.isCurrentlyVisible()));
    }
}
