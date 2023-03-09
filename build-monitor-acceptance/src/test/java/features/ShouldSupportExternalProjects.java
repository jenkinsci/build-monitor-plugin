package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.abilities.InteractWithJenkinsAPI;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions.NotifyOfExternalProject;
import net.serenitybdd.screenplay.jenkins.HaveAnExternalProjectCreated;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldSupportExternalProjects extends BuildMonitorAcceptanceTest {

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
                ProjectInformationMatchers.displaysProjectStatusAs(ProjectStatus.Successful)
        ));
    }
}
