package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayJunitRealtimeProgress;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAPipelineProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.Disable;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.GroovyScriptThat;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.SetPipelineDefinition;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldDisplayTestProgress extends BuildMonitorAcceptanceTest {

    Actor richard = Actor.named("Richard");

    @Before
    public void actorCanBrowseTheWeb() {
        richard.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void display_tests_progress_bar() {
        givenThat(richard).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAPipelineProjectCreated.called("My Pipeline").andConfiguredTo(
                        SetPipelineDefinition.asFollows(GroovyScriptThat.Pause_In_Middle_Of_Tests.code()),
                        Disable.executingConcurrentBuilds()
                ),

                ScheduleABuild.of("My Pipeline"),
                Navigate.to(jenkins.url()),
                ScheduleABuild.of("My Pipeline")
        );

        when(richard).attemptsTo(CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                DisplayAllProjects.usingARegularExpression(),
                DisplayJunitRealtimeProgress.bars()
        ));
        

        then(richard).should(seeThat(ProjectWidget.of("My Pipeline").testProgressBars(),
                WebElementStateMatchers.isCurrentlyVisible()
        ));
    }

}
