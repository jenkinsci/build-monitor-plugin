package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.openqa.selenium.By;

/**
 * @author Jan Molak
 */
public class ConfigureBuildMonitor implements Screen {
    public static ConfigureBuildMonitor screen() {
        return new ConfigureBuildMonitor();
    }

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
        return "view/Build Monitor/configure";
    }
}