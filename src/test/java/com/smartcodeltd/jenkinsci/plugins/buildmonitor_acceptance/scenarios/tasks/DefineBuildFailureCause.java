package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.bfa.FailureCauseManagement;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.bfa.NewFailureCause;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins.HomePage;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins.NewView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Action;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Click.click;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Enter.enter;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Navigate.navigateTo;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Submit.submit;

public class DefineBuildFailureCause implements Action {

    public static DefineBuildFailureCause defineABuildFailureCause(String name, String description, String pattern) {
        return new DefineBuildFailureCause(name, description, pattern);
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        for(Action action : configurationPlan) {
            action.executeUsing(jenkins, browser);
        }
    }

    private List<Action> configurationPlan;

    private DefineBuildFailureCause(final String name, final String description, final String pattern) {
        this.configurationPlan = new ArrayList<Action>() {{
            HomePage homePage = HomePage.screen();

            add(navigateTo(homePage));

            add(click(homePage.navigation().failureCauseManagement()));

            // transition

            add(click(FailureCauseManagement.screen().createNew()));

            // transition

            NewFailureCause failureCause = NewFailureCause.screen();
            add(enter(name, failureCause.name()));
            add(enter(description, failureCause.description()));

            add(click(failureCause.addIndication()));
            add(click(failureCause.buildLogIndication()));
            add(enter(pattern, failureCause.pattern()));

            add(submit(failureCause.save()));
        }};
    }
}
