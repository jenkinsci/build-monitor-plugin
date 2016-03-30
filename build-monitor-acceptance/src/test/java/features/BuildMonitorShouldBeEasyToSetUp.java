package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ConfigureEmptyBuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import environment.TestJenkinsInstance;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isCurrentlyVisible;

@RunWith(SerenityRunner.class)
public class BuildMonitorShouldBeEasyToSetUp {

    Actor anna = Actor.named("Anna");

    @Managed public WebDriver herBrowser;

    @Rule public JenkinsInstance jenkins = TestJenkinsInstance.withBuildMonitor().create();

    @Before
    public void actorCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    @Title("Adding a project to an empty Build Monitor")
    public void adding_project_to_an_empty_build_monitor() {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),
                HaveAProjectCreated.called("My Awesome App")
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor"),
                ConfigureEmptyBuildMonitorView.to(DisplayAllProjects.usingARegularExpression())
        );

        then(anna).should(seeThat(ProjectWidget.of("My Awesome App").state(), isCurrentlyVisible()));
    }
}
