package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayJunitRealtimeProgress;

import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAPipelineProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.Disable;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.SetPipelineDefinition;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.junit.annotations.TestData;

import org.junit.Before;
import org.junit.Test;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.GroovyScriptThat.Pause_In_Middle_Of_Tests;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isCurrentlyVisible;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShouldDisplayTestProgress extends BuilMonitorAcceptanceTest {

    Actor richard = Actor.named("Richard");

    public ShouldDisplayTestProgress(String jenkinsVersion) {
        super(jenkinsVersion);
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsBeforeStartRules() {
        return Collections.singletonList(InstallPlugins.fromCache(getpluginsCache(), "workflow-aggregator", "junit-realtime-test-reporter"));
    }

    @TestData
    public static Collection<Object[]> testData(){
        return BuilMonitorAcceptanceTest.testData();
    }

    @Before
    public void actorCanBrowseTheWeb() {
        richard.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void display_tests_progress_bar() throws Exception {
        givenThat(richard).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAPipelineProjectCreated.called("My Pipeline").andConfiguredTo(
                        SetPipelineDefinition.asFollows(Pause_In_Middle_Of_Tests.code()),
                        Disable.executingConcurrentBuilds()
                ),

                ScheduleABuild.of("My Pipeline"),
                ScheduleABuild.of("My Pipeline")
        );

        when(richard).attemptsTo(CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                DisplayAllProjects.usingARegularExpression(),
                DisplayJunitRealtimeProgress.bars()
        ));
        

        then(richard).should(seeThat(ProjectWidget.of("My Pipeline").testProgressBars(),
                isCurrentlyVisible()
        ));
    }

}
