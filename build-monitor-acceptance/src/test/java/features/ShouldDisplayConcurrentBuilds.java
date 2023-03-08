package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.Enable;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScript;
import net.serenitybdd.screenplayx.actions.Navigate;

import org.junit.Before;
import org.junit.Test;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.is;

public class ShouldDisplayConcurrentBuilds extends BuildMonitorAcceptanceTest {
    private static String My_App = "My App";

    Actor dave = Actor.named("Dave");

    @Before
    public void actorCanBrowseTheWeb() {
        dave.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void displaying_concurrent_builds() {
        givenThat(dave).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAProjectCreated.called(My_App).andConfiguredTo(
                        Enable.executingConcurrentBuilds(),
                        ExecuteAShellScript.that(sleepsFor(300))
                ),
                ScheduleABuild.of(My_App),
                Navigate.to(jenkins.url()),
                ScheduleABuild.of(My_App)
        );

        when(dave).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(dave).should(seeThat(ProjectWidget.of(My_App).executedBuilds(), is("#2\n#1")));
    }

    private ShellScript sleepsFor(int seconds) {
        return ShellScript.that("simulates executing a build").definedAs(
            String.format("sleep %d;", seconds)
        );
    }
}