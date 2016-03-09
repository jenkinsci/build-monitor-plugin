package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.openqa.selenium.By;

/**
 * @author Jan Molak
 */
public class NewView implements Screen {

    public static NewView screen() {
        return new NewView();
    }

    public By name() {
        return By.name("name");
    }

    public By mode(String name) {
        return By.xpath(String.format("//label/*[text()='%s']/../../input", name));
    }

    public By OK() {
        return By.xpath("//button[text() = 'OK']");
    }

    @Override
    public String path() {
        return "newView";
    }
}