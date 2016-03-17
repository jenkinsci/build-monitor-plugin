package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ConfigureBuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import net.serenitybdd.integration.browserstack.ConfigureBrowserStackSpecificCapabilities;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAFreestyleProject;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Title;
import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isCurrentlyVisible;

@RunWith(SerenityRunner.class)
public class BuildMonitorShouldBeEasyToSetUp {

    Actor anna = Actor.named("Anna");

    @Managed public WebDriver herBrowser;

    @Rule public TestRule browserstack = ConfigureBrowserStackSpecificCapabilities
            .forProject("Build Monitor Experiments").andBuild("1.8-SNAPSHOT");


    // Jenkins acceptance harness integration thingy. todo: Replace with something simpler.
    @Rule public JenkinsAcceptanceTestRule jenkinsATR = new JenkinsAcceptanceTestRule();
    @Inject public Jenkins jenkins;

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
                CreateAFreestyleProject.called("My Awesome App")
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor"),
                ConfigureBuildMonitorView.to(DisplayAllProjects.usingARegularExpression())
        );

        then(anna).should(seeThat(ProjectWidget.of("My Awesome App").state(), isCurrentlyVisible()));
    }
}
