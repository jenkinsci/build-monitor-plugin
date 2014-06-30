package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Action;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

public class Task implements Action {

    private List<Action> actions;

    public Task(Action... actions) {
        this.actions = Arrays.asList(actions);
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        for(Action action : actions) {
            action.executeUsing(jenkins, browser);
        }
    }
}
