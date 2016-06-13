package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import com.sonyericsson.jenkins.plugins.bfa.DefineABuildLogIndicatedFailureCause;
import com.sonyericsson.jenkins.plugins.bfa.HaveAShellScriptFailureCauseDefined;
import com.sonyericsson.jenkins.plugins.bfa.UseFailureCauseManagement;
import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplay.jenkins.HaveAProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScript;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScriptThat.Finishes_With_Error;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
public class ShouldTellWhatBrokeTheBuild {

    Actor dave = Actor.named("Dave");

    @Managed public WebDriver hisBrowser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().afterStart(
            InstallPlugins.fromUpdateCenter("cloudbees-folder", "build-failure-analyzer")
    ).create();

    @Before
    public void actorCanBrowseTheWeb() {
        dave.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void displaying_potential_failure_cause() throws Exception {
        givenThat(dave).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAShellScriptFailureCauseDefined.called("Rogue AI").describedAs("Pod bay doors didn't open"),
                HaveAFailingProjectCreated.called("Discovery One")
        );

        when(dave).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(dave).should(seeThat(ProjectWidget.of("Discovery One").details(), is("Identified problem: Pod bay doors didn't open")));
    }

    @Test
    public void displaying_the_number_of_failed_tests() throws Exception {
        givenThat(dave).wasAbleTo(
                Navigate.to(jenkins.url()),
                UseFailureCauseManagement.to(
                        DefineABuildLogIndicatedFailureCause.called("Unit tests failure").
                                describedAs("${1,2} of ${1,1} unit tests failed").
                                matching(".*Total: (\\d+).*Failed: ([1-9]\\d*).*")
                ),
                HaveAProjectCreated.called("My App").andConfiguredTo(
                        ExecuteAShellScript.that(hasXUnitFailures()),
                        ExecuteAShellScript.that(Finishes_With_Error)
                ),
                ScheduleABuild.of("My App")
        );

        when(dave).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(dave).should(seeThat(ProjectWidget.of("My App").details(),
                is("Identified problem: 5 of 143 unit tests failed")
        ));
    }

    private ShellScript hasXUnitFailures() {
        return ShellScript.that("simulates a failed XUnit test run").andOutputs(
            "TestSuite Total: 143, Errors: 0, Failed: 5, Skipped: 1, Time: 23.705s"
        );
    }
}
