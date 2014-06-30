package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions;

import org.openqa.selenium.By;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.WebDriver;

public class Click implements Action {
    public static Action click(By what) {
        return new Click(what);
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        browser.findElement(what).click();
    }


    private final By what;

    private Click(By what) {
        this.what = what;
    }
}