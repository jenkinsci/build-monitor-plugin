package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HideBadges;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ModifyControlPanelOptions;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ShowBadges;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.ConfigureViewSettings;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayBadges;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayBadgesFrom;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAPipelineProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.SetPipelineDefinition;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.junit.annotations.TestData;

import org.junit.Before;
import org.junit.Test;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.GroovyScriptThat.Adds_A_Badge;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isCurrentlyVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotCurrentlyVisible;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShouldDisplayBadges extends BuilMonitorAcceptanceTest {

    Actor paul = Actor.named("Paul");

    public ShouldDisplayBadges(String jenkinsVersion) {
        super(jenkinsVersion);
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsBeforeStartRules() {
        return Collections.singletonList(InstallPlugins.fromCache(getpluginsCache(), "workflow-aggregator", "badge"));
    }

    @TestData
    public static Collection<Object[]> testData(){
        return BuilMonitorAcceptanceTest.testData();
    }
    
    @Before
    public void actorCanBrowseTheWeb() {
        paul.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void user_displaying_build_badges() {
        givenThat(paul).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAPipelineProjectCreated.called("My App").andConfiguredTo(
                        SetPipelineDefinition.asFollows(Adds_A_Badge.code())
                ),

                ScheduleABuild.of("My App"),
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression(),
                        ConfigureViewSettings.doNotShowBadges(),
                        DisplayBadges.asAUserSetting(),
                        DisplayBadgesFrom.theLastBuild()
                )
        );

        when(paul).attemptsTo(ModifyControlPanelOptions.to(ShowBadges.onTheDashboard()));

        then(paul).should(seeThat(ProjectWidget.of("My App").badges(),
                isCurrentlyVisible()
        ));
    }

    @Test
    public void always_displaying_build_badges() {
        givenThat(paul).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAPipelineProjectCreated.called("My App").andConfiguredTo(
                        SetPipelineDefinition.asFollows(Adds_A_Badge.code())
                ),
                ScheduleABuild.of("My App"),
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression(),
                        ConfigureViewSettings.showBadges(),
                        DisplayBadges.always(),
                        DisplayBadgesFrom.theLastBuild()
                )
        );

        when(paul).attemptsTo(ModifyControlPanelOptions.to(HideBadges.onTheDashboard()));

        then(paul).should(seeThat(ProjectWidget.of("My App").badges(),
                isCurrentlyVisible()
        ));
    }

    @Test
    public void never_displaying_build_badges() {
        givenThat(paul).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAPipelineProjectCreated.called("My App").andConfiguredTo(
                        SetPipelineDefinition.asFollows(Adds_A_Badge.code())
                ),
                ScheduleABuild.of("My App"),
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression(),
                        ConfigureViewSettings.doNotShowBadges(),
                        DisplayBadges.never(),
                        DisplayBadgesFrom.theLastBuild()
                )
        );

        when(paul).attemptsTo(ModifyControlPanelOptions.to(ShowBadges.onTheDashboard()));

        then(paul).should(seeThat(ProjectWidget.of("My App").badges(), 
                isNotCurrentlyVisible()
        ));
    }
}
