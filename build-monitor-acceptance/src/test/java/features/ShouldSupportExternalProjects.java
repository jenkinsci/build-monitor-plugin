package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAnExternalProjectCreated;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.abilities.InteractWithJenkinsAPI;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions.NotifyOfExternalProject;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.junit.annotations.TestData;

import org.junit.Before;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers.ProjectInformationMatchers.displaysProjectStatusAs;
import static com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus.Successful;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShouldSupportExternalProjects extends BuilMonitorAcceptanceTest {

    Actor maggie = Actor.named("Maggie");

    public ShouldSupportExternalProjects(String jenkinsVersion) {
        super(jenkinsVersion);
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
        return Arrays.asList(InstallPlugins.fromUpdateCenter("external-monitor-job"));
    }

    @TestData
    public static Collection<Object[]> testData(){
        return BuilMonitorAcceptanceTest.testData();
    }
    
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
