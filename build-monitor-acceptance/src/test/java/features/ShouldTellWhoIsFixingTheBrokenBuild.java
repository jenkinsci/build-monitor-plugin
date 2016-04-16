package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import environment.JenkinsSandbox;
import hudson.plugins.claim.HaveAFailingClaimableProjectCreated;
import hudson.plugins.claim.tasks.Claim;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.integration.jenkins.environment.rules.RegisterUserAccount;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.JenkinsUser;
import net.serenitybdd.screenplay.jenkins.tasks.GoBack;
import net.serenitybdd.screenplay.jenkins.tasks.LogIn;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Claimed;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.core.Is.is;

@RunWith(SerenityRunner.class)
public class ShouldTellWhoIsFixingTheBrokenBuild {

    JenkinsUser ben = JenkinsUser.named("Ben");

    @Managed public WebDriver hisBrowser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().afterStart(
            InstallPlugins.fromUpdateCenter("claim"),
            RegisterUserAccount.of(ben)
    ).create();

    @Before
    public void actorCanBrowseTheWeb() {
        ben.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void claiming_a_broken_build() throws Exception {
        givenThat(ben).wasAbleTo(
                Navigate.to(jenkins.url()),
                LogIn.as(ben),
                HaveAFailingClaimableProjectCreated.called("Responsibly Developed")
        );

        when(ben).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression()
                ),
                Claim.lastBrokenBuildOf("Responsibly Developed").saying("My bad, fixing now"),
                GoBack.to("Build Monitor")
        );

        then(ben).should(seeThat(ProjectWidget.of("Responsibly Developed").information(), displaysProjectStatusAs(Claimed)));
        then(ben).should(seeThat(ProjectWidget.of("Responsibly Developed").details(),     is("Claimed by Ben: My bad, fixing now")));
    }
}
