package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.bfa.FailureCauseManagement;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.bfa.NewFailureCause;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.BuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.ConfigureBuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.Job;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins.HomePage;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins.NewView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.recipes.With;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// @TODO: Work in progress
public class BehaviourWithBuildFailureAnalyserTest extends AcceptanceTest {

    @Test
    @With(plugins = { "build-failure-analyzer-1.7.0.jpi", "git-1.5.0.hpi", "git-client-1.8.0.jpi", "ssh-credentials-1.6.1.jpi", "credentials-1.10.jpi", "scm-api-0.2.jpi" })
    public void displays_potential_failure_cause_when_it_is_known() throws Exception {
        // Given that Build Failure Analyzer knows what a 'shell script failure' looks like
        //  and a "Build Monitor" view exists showing all the jobs
        //  When 'example-acceptance' job fails because of a 'shell script failure'
        //  Then the Build Monitor presents the following:
        //    * 'example-acceptance' failed because of a 'shell script failure'

        // Given
        givenFollowingFailureCauseIsDefined(
                "Shell script failure",
                "A shell script failure happened",
                "Build step 'Execute shell' marked build as failure"
        );

        // todo: it's not clear that the job has failed due to a shell script failure
        givenABuildMonitorView("Build Monitor", ".*");

        // When
        givenFollowingProjectRunHasFailed("example-acceptance");

        // Then
        browser.get(urlFor("view/Build Monitor"));
        BuildMonitor buildMonitor = new BuildMonitor(browser.findElement(By.className("build-monitor")));

        Job exampleAcceptance = buildMonitor.job("example-acceptance");

        assertThat(exampleAcceptance.possibleFailureCause(), is("Shell script failure"));
    }


    // TASKS

    private void givenFollowingFailureCauseIsDefined(String name, String description, String pattern) throws IOException {
        HomePage homePage = HomePage.screen();
        navigateTo(homePage);

        click(on(homePage.navigation().failureCauseManagement()));

        // transition

        click(on(FailureCauseManagement.screen().createNew()));

        // transition

        NewFailureCause failureCause = NewFailureCause.screen();
        enter(name, as(failureCause.name()));
        enter(description, as(failureCause.description()));
        click(on(failureCause.addIndication()));
        click(on(failureCause.buildLogIndication()));
        enter(pattern, as(failureCause.pattern()));
        click(on(failureCause.save()));
    }

    private void givenABuildMonitorView(String name, String includes) throws IOException {
        NewView newView = NewView.screen();

        navigateTo(newView);

        // transition

        enter(name, as(newView.name()));
        click(on(newView.mode("Build Monitor View")));
        click(on(newView.OK()));

        ConfigureBuildMonitor configuration = ConfigureBuildMonitor.screen();

        click(on(configuration.useRegularExpressionToIncludeJobsIntoTheView()));
        enter(includes, as(configuration.regularExpression()));
        click(on(configuration.OK()));
    }


    // ACTIONS

    private void enter(String text, WebElement where) {
        where.clear();
        where.sendKeys(text);
    }

    private void navigateTo(Screen screen) throws IOException {
        browser.get(j.getURL().toString() + screen.path());
    }

    private void click(WebElement element) {
        element.click();
    }

    private WebElement as(By by) {
        return on(by);
    }

    private WebElement on(By by) {
        return browser.findElement(by);
    }
}