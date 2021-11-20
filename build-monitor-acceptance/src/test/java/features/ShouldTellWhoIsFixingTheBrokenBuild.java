package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import hudson.plugins.claim.HaveAFailingClaimableProjectCreated;
import hudson.plugins.claim.tasks.Claim;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.integration.jenkins.environment.rules.RegisterUserAccount;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.JenkinsUser;
import net.serenitybdd.screenplay.jenkins.tasks.GoBack;
import net.serenitybdd.screenplay.jenkins.tasks.LogIn;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.junit.annotations.TestData;

import org.junit.Before;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Claimed;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShouldTellWhoIsFixingTheBrokenBuild extends BuilMonitorAcceptanceTest {

    JenkinsUser ben;

    public ShouldTellWhoIsFixingTheBrokenBuild(String jenkinsVersion) {
        super(jenkinsVersion);
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsBeforeStartRules() {
        return Arrays.asList(InstallPlugins.fromCache(getpluginsCache(), "claim"));
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
        ben = JenkinsUser.named("Ben");
        return Arrays.asList(RegisterUserAccount.of(ben));
    }

    @TestData
    public static Collection<Object[]> testData(){
        return BuilMonitorAcceptanceTest.testData();
    }
    
    @Before
    public void actorCanBrowseTheWeb() {
        ben.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void claiming_a_broken_build() throws Exception {
        givenThat(ben).wasAbleTo(
                Navigate.to(jenkins.url()),
                LogIn.as(ben),
                HaveAFailingClaimableProjectCreated.called("Responsibly Developed")
        );

        when(ben).attemptsTo(
                HaveABuildMonitorViewCreated.showingAllTheProjects(),
                Claim.lastBrokenBuildOf("Responsibly Developed").saying("My bad, fixing now"),
                GoBack.to("Build Monitor")
        );

        then(ben).should(seeThat(ProjectWidget.of("Responsibly Developed").information(), displaysProjectStatusAs(Claimed)));
        then(ben).should(seeThat(ProjectWidget.of("Responsibly Developed").details(),     is("Claimed by Ben: My bad, fixing now")));
    }
}
