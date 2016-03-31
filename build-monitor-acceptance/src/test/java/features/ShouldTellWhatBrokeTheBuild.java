package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.sonyericsson.jenkins.plugins.bfa.user_interface.FailureCauseManagementPage;
import com.sonyericsson.jenkins.plugins.bfa.user_interface.JenkinsHomePageWithBFA;
import environment.TestJenkinsInstance;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Breadcrumbs;
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

    @Rule public JenkinsInstance jenkins = TestJenkinsInstance
            .withBuildMonitor()
            .withPlugins("cloudbees-folder", "build-failure-analyzer")  // BFA relies on Folder being available
            .create();

    @Before
    public void actorCanBrowseTheWeb() {
        dave.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void displays_potential_failure_cause_when_it_is_known() throws Exception {
        givenThat(dave).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),

                Click.on(JenkinsHomePageWithBFA.Failure_Cause_Management_Link),

                Click.on(FailureCauseManagementPage.Create_New),
                Enter.theValue("Rogue AI").into(FailureCauseManagementPage.Name),
                Enter.theValue("The pod bay doors won't open").into(FailureCauseManagementPage.Description),

                Click.on(FailureCauseManagementPage.Add_Indication),
                Click.on(FailureCauseManagementPage.Build_Log_Indication_Link),
                Enter.theValue("Build step 'Execute shell' marked build as failure").into(FailureCauseManagementPage.Pattern_Field),

                Click.on(FailureCauseManagementPage.Save),
                Click.on(Breadcrumbs.Jenkins_Link),

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
