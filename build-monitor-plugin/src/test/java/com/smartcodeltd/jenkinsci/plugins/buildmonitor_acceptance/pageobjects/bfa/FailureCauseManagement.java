package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.bfa;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.openqa.selenium.By;

/**
 * @author Jan Molak
 */
public class FailureCauseManagement implements Screen {

    public static FailureCauseManagement screen() {
        return new FailureCauseManagement();
    }

    @Override
    public String path() {
        return "/failure-cause-management/";
    }

    public By createNew() {
        return By.linkText("Create new");
    }
}