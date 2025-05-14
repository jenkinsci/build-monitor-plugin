package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Result;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.addProjectToView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.TestUtils.createAndRunJob;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.TestUtils.createFreeStyleProject;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
public class ShouldDisplayBadgesTest {

    @Test
    void test(Page p, JenkinsRule j) {
//        givenThat(paul)
//                .wasAbleTo(
//                        Navigate.to(jenkins.url()),
//                        HaveAPipelineProjectCreated.called("My App")
//                                .andConfiguredTo(SetPipelineDefinition.asFollows(GroovyScriptThat.Adds_A_Badge.code())),
//                        ScheduleABuild.of("My App"),
//                        CreateABuildMonitorView.called("Build Monitor")
//                                .andConfigureItTo(
//                                        DisplayAllProjects.usingARegularExpression(),
//                                        DisplayBadges.asAUserSetting(),
//                                        DisplayBadgesFrom.theLastBuild()));

        var run = createAndRunJob(j, "Lemonworld CI", "singleStagePipeline.jenkinsfile", Result.SUCCESS);

        var view = createBuildMonitorView(j, "Build Monitor");
        addProjectToView(run.getParent(), view);

        BuildMonitorViewPage.from(p, view).goTo().hasJobsCount(1).hasJob(run.getParent().getDisplayName());

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        when(paul).attemptsTo(ModifyControlPanelOptions.to(ShowBadges.onTheDashboard()));
//
//        then(paul).should(seeThat(ProjectWidget.of("My App").badges(), WebElementStateMatchers.isCurrentlyVisible()));
    }
}
