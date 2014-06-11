package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Jan Molak
 */
public class BuildMonitor extends PageObject {
    public BuildMonitor(WebElement root) {
        super(root);
    }

    public Job job(String name) {
        return new Job(root.findElement(By.id(name)));
    }
}