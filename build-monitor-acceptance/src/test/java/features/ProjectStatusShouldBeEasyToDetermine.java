package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateAndConfigureABuildMonitorView;
import net.serenitybdd.integration.browserstack.DescribeBrowserStackTestSession;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAProject;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
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

import static com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Failing;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Successful;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

@RunWith(SerenityRunner.class)
public class ProjectStatusShouldBeEasyToDetermine {

    Actor anna = Actor.named("Anna");

    @Managed public WebDriver herBrowser;

    @Rule public TestRule browserstack = DescribeBrowserStackTestSession
            .forProject("Build Monitor Experiments").andBuild("1.8-SNAPSHOT");


    // Jenkins acceptance harness integration thingy. todo: Replace with something simpler.
    @Rule public JenkinsAcceptanceTestRule jenkinsATR = new JenkinsAcceptanceTestRule();
    @Inject public Jenkins jenkins;

    @Before
    public void annaCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    @WithPlugins({"build-monitor-plugin"})
    public void visualising_a_successful_project() {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url),
                CreateAProject.called("My App").andScheduleABuildThatPasses()
        );

        when(anna).attemptsTo(
                CreateAndConfigureABuildMonitorView.called("Build Monitor").thatDisplaysAllTheProjects()
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
                CreateAProject.called("My App").andScheduleABuildThatFails()
        );

        when(anna).attemptsTo(
                CreateAndConfigureABuildMonitorView.called("Build Monitor").thatDisplaysAllTheProjects()
        );

        then(anna).should(seeThat(ProjectWidget.of("My App").information(),
                displaysProjectStatusAs(Failing)
        ));
    }
}
