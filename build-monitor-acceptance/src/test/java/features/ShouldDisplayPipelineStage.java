package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAPipelineProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.SetPipelineDefinition;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.junit.annotations.TestData;

import org.junit.Before;
import org.junit.Test;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.containsString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShouldDisplayPipelineStage extends BuilMonitorAcceptanceTest {

    Actor donald = Actor.named("Donald");

    public ShouldDisplayPipelineStage(String jenkinsVersion) {
        super(jenkinsVersion);
    }

    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
        return Arrays.asList(InstallPlugins.fromUpdateCenter("workflow-aggregator"));
    }

    @TestData
    public static Collection<Object[]> testData(){
        return BuilMonitorAcceptanceTest.testData();
    }
    
    @Before
    public void actorCanBrowseTheWeb() {
        donald.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void displaying_current_pipeline_stage() {
        givenThat(donald).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAPipelineProjectCreated.called("My Pipeline").andConfiguredTo(
                        SetPipelineDefinition.asFollows("stage('Compile') { sleep 50 }")
                ),

                ScheduleABuild.of("My Pipeline")
        );

        when(donald).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(donald).should(seeThat(ProjectWidget.of("My Pipeline").pipelineStages(),
                containsString("[Compile]")
        ));
    }

}
