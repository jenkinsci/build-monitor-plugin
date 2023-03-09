package features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import com.cloudbees.hudson.plugins.folder.HaveAFolderCreated;
import com.cloudbees.hudson.plugins.folder.HaveANestedProjectCreated;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.ProjectWidget;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.CreateABuildMonitorView;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayNestedProjects;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplayx.actions.Navigate;
import org.junit.Before;
import org.junit.Test;

public class ShouldSupportCloudBeesFolders extends BuildMonitorAcceptanceTest {

    Actor anna = Actor.named("Anna");

    @Before
    public void actorCanBrowseTheWeb() {
        anna.can(BrowseTheWeb.with(browser));
    }

    @Test
    public void visualising_projects_nested_in_folders() {

        givenThat(anna).wasAbleTo(
                Navigate.to(jenkins.url()),
                HaveAFolderCreated.called("Search Services").andInsideIt(
                        HaveANestedProjectCreated.called("Librarian"),
                        HaveAFolderCreated.called("Contracts").andInsideIt(
                                HaveANestedProjectCreated.called("Third Party System")
                        )
                ),
                Navigate.to(jenkins.url())
        );

        when(anna).attemptsTo(
                CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                        DisplayAllProjects.usingARegularExpression(),
                        DisplayNestedProjects.fromSubfolders()
                )
        );

        then(anna).should(seeThat(ProjectWidget.of("Search Services » Librarian").state(), WebElementStateMatchers.isVisible()));
        then(anna).should(seeThat(ProjectWidget.of("Search Services » Contracts » Third Party System").state(), WebElementStateMatchers.isVisible()));
    }
}
