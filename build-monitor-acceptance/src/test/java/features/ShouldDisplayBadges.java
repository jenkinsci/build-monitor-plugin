package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ModifyControlPanelOptions;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ShowBadges;
import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.AddAGroovyPostbuildScript;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.GroovyScriptThat;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScriptThat.Finishes_With_Success;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isCurrentlyVisible;

@RunWith(SerenityRunner.class)
//Broken
public class ShouldDisplayBadges {

    Actor paul = Actor.named("Paul");

    @Managed(driver = "chrome", options = "--lang=en") public WebDriver browser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().afterStart(
            InstallPlugins.fromUpdateCenter("buildtriggerbadge", "groovy-postbuild")
    ).create();

    @Before
    public void actorCanBrowseTheWeb() {
        paul.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void displaying_build_badges() throws Exception {
        givenThat(paul).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAProjectCreated.called("My App").andConfiguredTo(
                        ExecuteAShellScript.that(Finishes_With_Success),
                        AddAGroovyPostbuildScript.that(GroovyScriptThat.Adds_A_Badge)
                ),
                ScheduleABuild.of("My App"),
                HaveABuildMonitorViewCreated.showingAllTheProjects()
        );

        when(paul).attemptsTo(ModifyControlPanelOptions.to(ShowBadges.onTheDashboard()));

        then(paul).should(seeThat(ProjectWidget.of("My App").badges(),
        		isCurrentlyVisible()
        ));
    }
}
