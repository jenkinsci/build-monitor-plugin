package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import environment.TestJenkinsInstance;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplay.jenkins.HaveASuccessfulProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Failing;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Successful;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

@RunWith(SerenityRunner.class)
public class ProjectStatusShouldBeEasyToDetermine {

    Actor anna = Actor.named("Anna");

    @Managed public WebDriver herBrowser;

    @Rule public JenkinsInstance jenkins = TestJenkinsInstance.withBuildMonitor().create();

    @Before
    public void actorCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    @WithPlugins({"build-monitor-plugin"})
    public void visualising_a_successful_project() throws Exception {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),
                HaveASuccessfulProjectCreated.called("My App")
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
    public void visualising_a_failing_project() throws Exception {

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),
                HaveAFailingProjectCreated.called("My App")
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
