package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.openqa.selenium.By;

/**
 * @author Jan Molak
 */
public class ConfigureBuildMonitor implements Screen {
    public static ConfigureBuildMonitor screen() {
        return new ConfigureBuildMonitor("Build Monitor");
    }

    public static ConfigureBuildMonitor screen(String name) {
        return new ConfigureBuildMonitor(name);
    }

    public By recurseInSubfolders() { return By.id("recurse"); }

    public By useRegularExpressionToIncludeJobsIntoTheView() {
        return By.name("useincluderegex");
    }

    public By regularExpression() {
        return By.name("includeRegex");
    }

    public By OK() {
        return By.xpath("//button[text() = 'OK']");
    }

    @Override
    public String path() {
        return "view/" + name + "/configure";
    }


    private final String name;

    private ConfigureBuildMonitor(String name) {
        this.name = name;
    }

}