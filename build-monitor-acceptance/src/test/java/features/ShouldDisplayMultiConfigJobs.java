package features;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.ConfigureEmptyBuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayMultiConfig;
import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.HaveAMultiConfigProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.AddAxis;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.Axis;
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
public class ShouldDisplayMultiConfigJobs {

    Actor mason = Actor.named("Mason");

    Axis numbers = new Axis("Numbers", new String[] {"1", "2"});

    @Managed public WebDriver hisBrowser;

    @Rule
    public JenkinsInstance jenkins = JenkinsSandbox.configure().create();

    @Before
    public void actorCanBrowseTheWeb() {
        mason.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void displaying_two_configuration_widgets() throws Exception {

        givenThat(mason).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAMultiConfigProjectCreated.called("MultiProject")
                    .andConfigureItTo(AddAxis.of(numbers))
        );

        when(mason).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor"),
                ConfigureEmptyBuildMonitorView.to(DisplayMultiConfig.usingDisplayMultiConfigCheckbox(), DisplayAllProjects.usingARegularExpression())
        );

        then(mason).should(seeThat(ProjectWidget.of("MultiProject").multiConfigBadgeNames(), is("MultiProject » 1:::MultiProject » 2")));
    }
}
