package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import com.sonyericsson.jenkins.plugins.bfa.HaveAShellScriptFailureCauseDefined;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.junit.annotations.TestData;

import org.junit.Before;
import org.junit.Test;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.containsString;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShouldTellWhatBrokeTheBuild extends BuilMonitorAcceptanceTest {

    Actor dave = Actor.named("Dave");

    public ShouldTellWhatBrokeTheBuild(String jenkinsVersion) {
        super(jenkinsVersion);
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsBeforeStartRules() {
        return Collections.singletonList(InstallPlugins.fromCache(getpluginsCache(), "workflow-aggregator", "cloudbees-folder", "build-failure-analyzer"));
    }

    @TestData
    public static Collection<Object[]> testData(){
        return BuilMonitorAcceptanceTest.testData();
    }
    
    @Before
    public void actorCanBrowseTheWeb() {
        dave.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void displaying_potential_failure_cause() {
        givenThat(dave).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAShellScriptFailureCauseDefined.called("Rogue AI").describedAs("Pod bay doors didn't open"),
                HaveAFailingProjectCreated.called("Discovery One")
        );

        when(dave).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(dave).should(seeThat(ProjectWidget.of("Discovery One").details(),
                containsString("Identified problem: Rogue AI")
        ));
    }
}
