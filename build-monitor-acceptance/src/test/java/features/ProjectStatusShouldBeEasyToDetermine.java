package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateAndConfigureABuildMonitorView;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAProject;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
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

    @Managed        //(uniqueSession = true)
    public WebDriver herBrowser;

    @Rule
    public JenkinsAcceptanceTestRule rules = new JenkinsAcceptanceTestRule();

    // Jenkins acceptance harness integration thingy. todo: Replace with something simpler.
    @Inject
    public Jenkins jenkins;

    // fixme: hack
    @Rule public TestName name = new TestName();

    @Before
    public void annaCanBrowseTheWeb() {

        anna.can(BrowseTheWeb.with(herBrowser));

        // fixme: hack

        Injectors.getInjector().getInstance(EnvironmentVariables.class).setProperty("browserstack.name", name.getMethodName());
        Injectors.getInjector().getInstance(EnvironmentVariables.class).setProperty("browserstack.project", "Build Monitor");
        Injectors.getInjector().getInstance(EnvironmentVariables.class).setProperty("browserstack.build", "1.8-SNAPSHOT");
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
