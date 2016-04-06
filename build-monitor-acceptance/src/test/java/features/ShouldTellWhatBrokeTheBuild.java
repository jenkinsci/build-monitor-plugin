package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.sonyericsson.jenkins.plugins.bfa.HaveAShellScriptFailureCauseDefined;
import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.InstallPlugins;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
public class ShouldTellWhatBrokeTheBuild {

    Actor dave = Actor.named("Dave");

    @Managed public WebDriver hisBrowser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().afterStart(
            InstallPlugins.fromUpdateCenter("cloudbees-folder", "build-failure-analyzer")
    ).create();

    @Before
    public void actorCanBrowseTheWeb() {
        dave.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void displays_potential_failure_cause_when_it_is_known() throws Exception {
        givenThat(dave).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),
                HaveAShellScriptFailureCauseDefined.called("Rogue AI"),
                HaveAFailingProjectCreated.called("Discovery One")
        );

        when(dave).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression()
                )
        );

        then(dave).should(seeThat(ProjectWidget.of("Discovery One").details(), is("Identified problem: Rogue AI")));
    }
}
