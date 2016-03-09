package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions;

import org.openqa.selenium.By;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

public class Submit implements Action {
    public static Action submit(By what) {
        return new Submit(what);
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        browser.findElement(what).submit();
    }

    private final By what;

    private Submit(By what) {
        this.what = what;
    }
}
