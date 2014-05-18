package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.bfa;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.openqa.selenium.By;

/**
 * @author Jan Molak
 */
public class NewFailureCause implements Screen {
    public static NewFailureCause screen() {
        return new NewFailureCause();
    }

    public By name() {
        return By.name("_.name");
    }

    public By description() {
        return By.name("_.description");
    }

    public By save() {
        return By.xpath("//button[text() = 'Save']");
    }

    public By addIndication() {
        return By.xpath("//button[text() = 'Add Indication']");
    }

    public By buildLogIndication() {
        return By.linkText("Build Log Indication");
    }

    public By pattern() {
        return By.name("pattern");
    }

    @Override
    public String path() {
        return "failure-cause-management/new/";
    }
}