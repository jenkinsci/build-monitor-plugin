package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.hamcrest.core.Is.is;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import hudson.plugins.claim.HaveAFailingClaimableProjectCreated;
import hudson.plugins.claim.tasks.Claim;
import java.util.List;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.RegisterUserAccount;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.JenkinsUser;
import net.serenitybdd.screenplay.jenkins.tasks.GoBack;
import net.serenitybdd.screenplay.jenkins.tasks.LogIn;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldTellWhoIsFixingTheBrokenBuild extends BuildMonitorAcceptanceTest {

    JenkinsUser ben;

    @Override
    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
        ben = JenkinsUser.named("Ben");
        return List.of(RegisterUserAccount.of(ben));
    }

    @Before
    public void actorCanBrowseTheWeb() {
        ben.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void claiming_a_broken_build() {
        givenThat(ben)
                .wasAbleTo(
                        Navigate.to(jenkins.url()),
                        LogIn.as(ben),
                        HaveAFailingClaimableProjectCreated.called("Responsibly Developed"));

        when(ben)
                .attemptsTo(
                        HaveABuildMonitorViewCreated.showingAllTheProjects(),
                        Claim.lastBrokenBuildOf("Responsibly Developed").saying("My bad, fixing now"),
                        GoBack.to("Build Monitor"));

        then(ben)
                .should(seeThat(
                        ProjectWidget.of("Responsibly Developed").information(),
                        ProjectInformationMatchers.displaysProjectStatusAs(ProjectStatus.Claimed)));
        then(ben)
                .should(seeThat(
                        ProjectWidget.of("Responsibly Developed").details(), is("Claimed by Ben: My bad, fixing now")));
    }
}
