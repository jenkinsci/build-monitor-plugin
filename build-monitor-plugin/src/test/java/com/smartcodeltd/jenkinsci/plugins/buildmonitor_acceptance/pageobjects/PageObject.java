package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects;

import org.openqa.selenium.WebElement;

/**
 * @author Jan Molak
 */
public abstract class PageObject {
    protected final WebElement root;

    public PageObject(WebElement root) {
        this.root = root;
    }
}