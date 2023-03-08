package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAnExternalProjectCreated;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.abilities.InteractWithJenkinsAPI;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions.NotifyOfExternalProject;
import net.serenitybdd.screenplayx.actions.Navigate;

import org.junit.Before;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Successful;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

public class ShouldSupportExternalProjects extends BuilMonitorAcceptanceTest {

    Actor maggie = Actor.named("Maggie");

    @Before
    public void actorCanBrowseTheWeb() {
        maggie.can(BrowseTheWeb.with(browser))
              .can(InteractWithJenkinsAPI.using(jenkins.client()));
    }

    @Test
    public void visualising_an_external_project() {

        givenThat(maggie).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAnExternalProjectCreated.called("external-project")
        );

        when(maggie).attemptsTo(
                NotifyOfExternalProject.successOf("external-project"),
                HaveABuildMonitorViewCreated.showingAllTheProjects()
        );

        then(maggie).should(seeThat(ProjectWidget.of("external-project").information(),
                displaysProjectStatusAs(Successful)
        ));
    }
}
