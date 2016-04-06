package features;

import environment.JenkinsSandbox;
import hudson.plugins.claim.HaveAFailingClaimableProjectCreated;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;

@RunWith(SerenityRunner.class)
@Ignore
public class ShouldTellWhoIsFixingTheBrokenBuild {

    Actor ben = Actor.named("Ben");

    @Managed public WebDriver hisBrowser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().afterStart(
            InstallPlugins.fromUpdateCenter("claim")
    ).create();

    @Before
    public void actorCanBrowseTheWeb() {
        ben.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void name() throws Exception {
        givenThat(ben).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),
                HaveAFailingClaimableProjectCreated.called("Responsibly Developed App")
        );



//        Thread.sleep(5 * 60 * 1000);
    }
}
