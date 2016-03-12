package features;

import build_monitor.questions.ProjectWidget;
import build_monitor.tasks.ConfigureBuildMonitorView;
import build_monitor.tasks.CreateABuildMonitorView;
import core_jenkins.tasks.CreateAFreestyleProject;
import core_jenkins.tasks.Start;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Title;
import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isCurrentlyVisible;

@RunWith(SerenityRunner.class)
public class ShouldBeEasyToSetUp {

    Actor anna = Actor.named("Anna");

    @Managed(uniqueSession = true)
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
    @Title("Adding a project to an empty Build Monitor")
    @WithPlugins({"build-monitor-plugin"})
    public void adding_project_to_an_empty_build_monitor() {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url),
                CreateAFreestyleProject.called("my awesome app")
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor"),
                // todo: configure in a context: .and(ConfigureBuildMonitorView....., ... )
                ConfigureBuildMonitorView.toDisplayAllProjects()
        );

        // todo: create a test asserting on status
//        then(anna).should(seeThat(ProjectWidget.of("my awesome app").information(),
//                displays("name",   equalTo("my awesome app")),
//                displays("status", hasItem(ProjectStatus.Unknown))
//        ));

        then(anna).should(seeThat(ProjectWidget.of("my awesome app").state(), isCurrentlyVisible()));
    }
}
