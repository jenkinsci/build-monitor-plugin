package features;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.page.TheWebPage;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import tasks.OpenJenkinsHomePage;
import tasks.OpenTheApplication;

import javax.inject.Inject;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.containsString;

@RunWith(SerenityRunner.class)
public class BuildMonitorSerenityIntegrationTest {

    Actor anna = Actor.named("Anna");

    @Managed(uniqueSession = true)
    public WebDriver herBrowser;

    @Steps
    OpenTheApplication openTheApplication;

    @Rule
    public JenkinsAcceptanceTestRule rules = new JenkinsAcceptanceTestRule();

    /**
     * Jenkins under test.
     */
    @Inject
    public Jenkins jenkins;


    @Before
    public void annaCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Issue("#Jenkins-1")
    @Test
    public void jenkins_acceptance_harness_and_serenity_should_work_together() {

        givenThat(anna).wasAbleTo(OpenJenkinsHomePage.at(jenkins.url("")));

        then(anna).should(eventually(seeThat(TheWebPage.title(), containsString("Jenkins"))));
    }
}
