package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HideBadges;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ModifyControlPanelOptions;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ShowBadges;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.ConfigureViewSettings;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayBadges;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayBadgesFrom;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAPipelineProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.GroovyScriptThat;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.SetPipelineDefinition;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldDisplayBadges extends BuildMonitorAcceptanceTest {

    Actor paul = Actor.named("Paul");

    @Before
    public void actorCanBrowseTheWeb() {
        paul.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void user_displaying_build_badges() {
        givenThat(paul)
                .wasAbleTo(
                        Navigate.to(jenkins.url()),
                        HaveAPipelineProjectCreated.called("My App")
                                .andConfiguredTo(SetPipelineDefinition.asFollows(GroovyScriptThat.Adds_A_Badge.code())),
                        ScheduleABuild.of("My App"),
                        CreateABuildMonitorView.called("Build Monitor")
                                .andConfigureItTo(
                                        DisplayAllProjects.usingARegularExpression(),
                                        DisplayBadges.asAUserSetting(),
                                        DisplayBadgesFrom.theLastBuild()));

        when(paul).attemptsTo(ModifyControlPanelOptions.to(ShowBadges.onTheDashboard()));

        then(paul).should(seeThat(ProjectWidget.of("My App").badges(), WebElementStateMatchers.isCurrentlyVisible()));
    }

    @Test
    public void always_displaying_build_badges() {
        givenThat(paul)
                .wasAbleTo(
                        Navigate.to(jenkins.url()),
                        HaveAPipelineProjectCreated.called("My App")
                                .andConfiguredTo(SetPipelineDefinition.asFollows(GroovyScriptThat.Adds_A_Badge.code())),
                        ScheduleABuild.of("My App"),
                        CreateABuildMonitorView.called("Build Monitor")
                                .andConfigureItTo(
                                        DisplayAllProjects.usingARegularExpression(),
                                        ConfigureViewSettings.toggleShowBadges(),
                                        DisplayBadges.always(),
                                        DisplayBadgesFrom.theLastBuild()));

        when(paul).attemptsTo(ModifyControlPanelOptions.to(HideBadges.onTheDashboard()));

        then(paul).should(seeThat(ProjectWidget.of("My App").badges(), WebElementStateMatchers.isCurrentlyVisible()));
    }

    @Test
    public void never_displaying_build_badges() {
        givenThat(paul)
                .wasAbleTo(
                        Navigate.to(jenkins.url()),
                        HaveAPipelineProjectCreated.called("My App")
                                .andConfiguredTo(SetPipelineDefinition.asFollows(GroovyScriptThat.Adds_A_Badge.code())),
                        ScheduleABuild.of("My App"),
                        CreateABuildMonitorView.called("Build Monitor")
                                .andConfigureItTo(
                                        DisplayAllProjects.usingARegularExpression(),
                                        DisplayBadges.never(),
                                        DisplayBadgesFrom.theLastBuild()));

        when(paul).attemptsTo(ModifyControlPanelOptions.to(ShowBadges.onTheDashboard()));

        then(paul)
                .should(seeThat(ProjectWidget.of("My App").badges(), WebElementStateMatchers.isNotCurrentlyVisible()));
    }
}
