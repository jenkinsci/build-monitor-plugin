package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions;

import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

public interface Action {
    void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception;
}
