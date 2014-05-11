package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BuildMonitor {
    private final WebElement root;

    public BuildMonitor(WebElement root) {
        this.root = root;
    }

    public Job job(String jobId) {
        return new Job(root.findElement(By.id(jobId)));
    }
}