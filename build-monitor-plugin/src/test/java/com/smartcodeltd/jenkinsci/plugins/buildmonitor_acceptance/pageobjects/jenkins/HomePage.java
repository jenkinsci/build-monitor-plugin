package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.jenkins;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.Screen;
import org.openqa.selenium.By;

/**
 * @author Jan Molak
 */
public class HomePage implements Screen {

    @Override
    public String path() {
        return "/";
    }

    public Navigation navigation() {
        return new Navigation();
    }

    public By newView() {
        return By.linkText("+");
    }

    public static HomePage screen() {
        return new HomePage();
    }
}