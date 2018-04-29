package features;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ConfigureEmptyBuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.CollapseSuccessfulBuilds;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAFailingProjectCreated;
import net.serenitybdd.screenplay.jenkins.HaveASuccessfulProjectCreated;
import net.serenitybdd.screenplayx.actions.Navigate;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
public class BVMCollapseSummaryBadgeTest {

    Actor sophie = Actor.named("Sophie");

    @Managed public WebDriver herBrowser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().create();

    @Before
    public void actorCanBrowseTheWeb() {
        sophie.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    public void one_of_one_tests_pass_full_screen_summary_badge() throws Exception {

        givenThat(sophie).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveASuccessfulProjectCreated.called("My Project")
        );

        when(sophie).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor"),
                ConfigureEmptyBuildMonitorView.to(CollapseSuccessfulBuilds.usingCollapseSuccessfulBuildsCheckbox(),DisplayAllProjects.usingARegularExpression())
                );

        then(sophie).should(seeThat(ProjectWidget.of("My Project").fullScreenSummaryBadgeDescription(), is("all builds passed")));

    }

    @Test
    public void one_of_two_tests_pass_partial_screen_summary_badge() throws Exception {

        givenThat(sophie).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveASuccessfulProjectCreated.called("Successful Project"),
                Navigate.to(jenkins.url()),
                HaveAFailingProjectCreated.called("Failing Project")
        );

        when(sophie).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor"),
                ConfigureEmptyBuildMonitorView.to(CollapseSuccessfulBuilds.usingCollapseSuccessfulBuildsCheckbox(),DisplayAllProjects.usingARegularExpression())
        );

        then(sophie).should(seeThat(ProjectWidget.of("").partialScreenSummaryBadgeDescription(), is("1/2 job(s) passing")));

    }
}
