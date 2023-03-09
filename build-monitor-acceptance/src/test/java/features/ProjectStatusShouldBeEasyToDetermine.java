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
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplay.jenkins.HaveASuccessfulProjectCreated;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ProjectStatusShouldBeEasyToDetermine extends BuildMonitorAcceptanceTest {

    private Actor anna = Actor.named("Anna");

    @Before
    public void actorCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void visualising_a_successful_project() {

        givenThat(anna).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveASuccessfulProjectCreated.called("My App")
        );

        when(anna).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(anna).should(seeThat(ProjectWidget.of("My App").information(),
                ProjectInformationMatchers.displaysProjectStatusAs(ProjectStatus.Successful)
        ));
    }

    @Test
    public void visualising_a_failing_project() {

        givenThat(anna).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAFailingProjectCreated.called("My App")
        );

        when(anna).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(anna).should(seeThat(ProjectWidget.of("My App").information(),
                ProjectInformationMatchers.displaysProjectStatusAs(ProjectStatus.Failing)
        ));
    }
}
