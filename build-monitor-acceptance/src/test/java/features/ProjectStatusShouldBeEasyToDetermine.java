package features;

import build_monitor.questions.ProjectWidget;
import build_monitor.tasks.CreateABuildMonitorView;
import build_monitor.tasks.configuration.DisplayAllProjects;
import core_jenkins.tasks.CreateAFreestyleProject;
import core_jenkins.tasks.ScheduleABuild;
import core_jenkins.tasks.Start;
import core_jenkins.tasks.configuration.ExecuteAShellScript;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;
import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static build_monitor.model.ProjectStatus.Failing;
import static build_monitor.model.ProjectStatus.Successful;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

@RunWith(SerenityRunner.class)
public class ProjectStatusShouldBeEasyToDetermine {

    Actor anna = Actor.named("Anna");

    @Managed        //(uniqueSession = true)
    public WebDriver herBrowser;

    @Rule
    public JenkinsAcceptanceTestRule rules = new JenkinsAcceptanceTestRule();

    // Jenkins acceptance harness integration thingy. todo: Replace with something simpler.
    @Inject
    public Jenkins jenkins;

    @Before
    public void annaCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    @WithPlugins({"build-monitor-plugin"})
    public void visualising_a_successful_project() {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url),
                CreateAFreestyleProject.called("My App").andConfigureItTo(      // todo: too much nesting, extract to "pre-configured" configurations
                        ExecuteAShellScript.thatPasses()
                ),
                ScheduleABuild.of("My App")                                     // todo: should wait for the build to finish
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression()
                )
        );

        then(anna).should(seeThat(ProjectWidget.of("My App").information(),
                displaysProjectStatusAs(Successful)
        ));
    }

    @Test
    @WithPlugins({"build-monitor-plugin"})
    public void visualising_a_failing_project() {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url),
                CreateAFreestyleProject.called("My App").andConfigureItTo(      // todo: too much nesting, extract to "pre-configured" configurations
                        ExecuteAShellScript.thatFails()
                ),
                ScheduleABuild.of("My App")                                     // todo: should wait for the build to finish
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression()
                )
        );

        then(anna).should(seeThat(ProjectWidget.of("My App").information(),
                displaysProjectStatusAs(Failing)
        ));
    }
}
