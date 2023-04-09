package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.hamcrest.Matchers.containsString;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import com.sonyericsson.jenkins.plugins.bfa.HaveAShellScriptFailureCauseDefined;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldTellWhatBrokeTheBuild extends BuildMonitorAcceptanceTest {

    Actor dave = Actor.named("Dave");

    @Before
    public void actorCanBrowseTheWeb() {
        dave.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void displaying_potential_failure_cause() {
        givenThat(dave)
                .wasAbleTo(
                        Navigate.to(jenkins.url()),
                        HaveAShellScriptFailureCauseDefined.called("Rogue AI").describedAs("Pod bay doors didn't open"),
                        HaveAFailingProjectCreated.called("Discovery One"));

        when(dave).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(dave)
                .should(seeThat(
                        ProjectWidget.of("Discovery One").details(), containsString("Identified problem: Rogue AI")));
    }
}
