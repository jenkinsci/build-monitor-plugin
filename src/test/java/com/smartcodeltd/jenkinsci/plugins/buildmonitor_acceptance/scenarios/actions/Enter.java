package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.actions;

import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Enter implements Action {
    public static Action enter(String what, By where) {
        return new Enter(what, where);
    }

    @Override
    public void executeUsing(JenkinsRule jenkins, WebDriver browser) throws Exception {
        WebElement element = browser.findElement(where);

        element.clear();
        element.sendKeys(what);
    }


    private final String what;
    private final By where;

    private Enter(String what, By where) {
        this.what = what;
        this.where = where;
    }
}
