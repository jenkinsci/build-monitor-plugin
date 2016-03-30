package features;

import com.cloudbees.hudson.plugins.folder.HaveAFolderCreated;
import com.cloudbees.hudson.plugins.folder.HaveANestedProjectCreated;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayNestedProjects;
import environment.TestJenkinsInstance;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

@RunWith(SerenityRunner.class)
public class ShouldSupportCloudBeesFolders {

    Actor anna = Actor.named("Anna");

    @Managed public WebDriver herBrowser;

    @Rule public JenkinsInstance jenkins = TestJenkinsInstance.withBuildMonitor().create();

    @Before
    public void actorCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    public void visualising_projects_nested_in_folders() throws Exception {

        // todo: refactor and encapsulate
        jenkins.client().installPlugin("cloudbees-folder");

        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url()),
                HaveAFolderCreated.called("Search Services").andInsideIt(
                        HaveANestedProjectCreated.called("Librarian"),
                        HaveAFolderCreated.called("Contracts").andInsideIt(
                                HaveANestedProjectCreated.called("Third Party System")
                        )
                )
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression(),
                        DisplayNestedProjects.fromSubfolders()
                )
        );

        then(anna).should(seeThat(ProjectWidget.of("Search Services » Librarian").state(), isVisible()));
        then(anna).should(seeThat(ProjectWidget.of("Search Services » Contracts » Third Party System").state(), isVisible()));
    }
}
