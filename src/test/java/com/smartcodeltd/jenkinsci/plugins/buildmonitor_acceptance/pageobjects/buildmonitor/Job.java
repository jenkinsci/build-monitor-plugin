package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Job {
    private final WebElement root;

    public Job(WebElement root) {
        this.root = root;
    }

    public String name() {
        return root.findElement(By.tagName("h2")).getText();
    }

    public String status() {
        return root.getAttribute("class");
    }

    public String possibleFailureCause() {
        return root.findElement(By.className("failures")).getText();
    }
}