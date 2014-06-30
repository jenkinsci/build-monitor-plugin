package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.tasks;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.ConfigureBuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Action;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Click.click;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions.Enter.enter;

public class ConfigureJobFilters implements Action {

    public static Action includesAllTheJobs() {
        return new ConfigureJobFilters(new ArrayList<Action>() {{
            ConfigureBuildMonitor configuration = ConfigureBuildMonitor.screen();

            add(click(configuration.useRegularExpressionToIncludeJobsIntoTheView()));
            add(enter(".*", configuration.regularExpression()));
        }});
    }

    public static Action recursesInSubFolders() {
        return new ConfigureJobFilters(new ArrayList<Action>() {{
            add(click(ConfigureBuildMonitor.screen().recurseInSubfolders()));
        }});
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        for (Action action : actions) {
            action.executeUsing(jenkins, browser);
        }
    }


    private List<Action> actions = new ArrayList<Action>();

    private ConfigureJobFilters(List<Action> actions) {
        this.actions = actions;
    }
}
