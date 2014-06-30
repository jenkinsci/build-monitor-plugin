package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

public class Navigate implements Action {
    public static Action navigateTo(Screen screen) {
        return new Navigate(screen);
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        browser.get(jenkins.getURL().toString() + screen.path());
    }


    private final Screen screen;

    private Navigate(Screen screen) {
        this.screen = screen;
    }
}