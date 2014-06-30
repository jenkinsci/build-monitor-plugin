package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.ConfigureBuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins.NewView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Action;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Click.click;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Enter.enter;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Navigate.navigateTo;

public class CreateBuildMonitorView implements Action {

    public static CreateBuildMonitorView createABuildMonitorView(String name) {
        return new CreateBuildMonitorView(name);
    }

    public Action that(Action... configurationPlans) {
        configurationPlan.addAll(Arrays.asList(configurationPlans));

        return this;
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        configurationPlan.add(click(ConfigureBuildMonitor.screen().OK()));

        for(Action action : configurationPlan) {
            action.executeUsing(jenkins, browser);
        }
    }


    private List<Action> configurationPlan;

    private CreateBuildMonitorView(final String name) {
        this.configurationPlan = new ArrayList<Action>() {{
            NewView newView = NewView.screen();

            add(navigateTo(newView));
            add(enter(name, newView.name()));
            add(click(newView.mode("Build Monitor View")));
            add(click(newView.OK()));
        }};
    }
}