package features;

import com.cloudbees.hudson.plugins.folder.HaveAFolderCreated;
import com.cloudbees.hudson.plugins.folder.HaveANestedProjectCreated;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayNestedProjects;
import net.serenitybdd.integration.browserstack.DescribeBrowserStackTestSession;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.jenkins.tasks.Start;
import net.thucydides.core.annotations.Managed;
import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

@RunWith(SerenityRunner.class)
// todo: will this still install the plugin if we already have one
@WithPlugins({"build-monitor-plugin", "cloudbees-folder"})
public class ShouldSupportCloudBeesFolders {

    Actor anna = Actor.named("Anna");

    @Managed public WebDriver herBrowser;

    @Rule public TestRule browserstack = DescribeBrowserStackTestSession
            .forProject("Build Monitor Experiments").andBuild("1.8-SNAPSHOT");


    // Jenkins acceptance harness integration thingy. todo: Replace with something simpler.
    @Rule public JenkinsAcceptanceTestRule jenkinsATR = new JenkinsAcceptanceTestRule();
    @Inject public Jenkins jenkins;

    @Before
    public void annaCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(herBrowser));
    }

    @Test
    public void visualising_projects_nested_in_folders() throws Exception {
        givenThat(anna).wasAbleTo(
                Start.withJenkinsAt(jenkins.url),
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
