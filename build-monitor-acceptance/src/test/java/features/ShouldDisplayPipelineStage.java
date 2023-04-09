package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.hamcrest.Matchers.containsString;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.HaveABuildMonitorViewCreated;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAPipelineProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.SetPipelineDefinition;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldDisplayPipelineStage extends BuildMonitorAcceptanceTest {

    Actor donald = Actor.named("Donald");

    @Before
    public void actorCanBrowseTheWeb() {
        donald.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void displaying_current_pipeline_stage() {
        givenThat(donald)
                .wasAbleTo(
                        Navigate.to(jenkins.url()),
                        HaveAPipelineProjectCreated.called("My Pipeline")
                                .andConfiguredTo(SetPipelineDefinition.asFollows("stage('Compile') { sleep 50 }")),
                        ScheduleABuild.of("My Pipeline"));

        when(donald).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());

        then(donald).should(seeThat(ProjectWidget.of("My Pipeline").pipelineStages(), containsString("[Compile]")));
    }
}
