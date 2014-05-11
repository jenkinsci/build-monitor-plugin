package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.saucelabs.common.SauceOnDemandAuthentication;
import hudson.model.FreeStyleProject;
import org.junit.runner.Description;
import hudson.tasks.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

abstract public class AcceptanceTest {
    @Rule
    public final JenkinsRule j = new JenkinsRule();

    protected WebDriver browser;

    private final SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    @Before
    public void setUp() throws Exception {
        browser = browser();
    }

    @After
    public void tearDown() throws Exception {
        browser.quit();
    }

    protected void givenFollowingProjectRunHasSucceeded(String projectName) throws IOException {
        FreeStyleProject exampleAcceptanceProject = j.createFreeStyleProject(projectName);
        exampleAcceptanceProject.getBuildersList().add(new Shell("exit 0"));
        exampleAcceptanceProject.scheduleBuild2(0);
    }

    protected void givenFollowingProjectRunHasFailed(String projectName) throws IOException {
        FreeStyleProject exampleBuildProject = j.createFreeStyleProject(projectName);
        exampleBuildProject.getBuildersList().add(new Shell("exit 1"));
        exampleBuildProject.scheduleBuild2(0);
    }

    protected String urlFor(String path) throws IOException {
        return j.getURL().toString() + path;
    }

    private WebDriver browser() throws MalformedURLException {
        return shouldUseRemoteBrowser() ?
                sauceLabsBrowser() :
                localChromeBrowser();
    }

    protected boolean shouldUseRemoteBrowser() {
        return ! (authentication.getUsername().isEmpty() || authentication.getAccessKey().isEmpty());
    }

    private WebDriver sauceLabsBrowser() throws MalformedURLException {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("platform", Platform.MAC);
        capabilities.setCapability("name", currentTestCaseName());

        return new RemoteWebDriver(
            new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@localhost:4445/wd/hub"),
            capabilities
        );
    }

    private String currentTestCaseName() {
        String testCaseName = "";

        try {
            Description desc = j.getTestDescription();
            testCaseName = desc.getTestClass().getMethod(desc.getMethodName()).getName().toString();
        } catch (NoSuchMethodException e) {
            testCaseName = this.getClass().getSimpleName();
        }

        return testCaseName.replaceAll(
                "_",
                " "
        );
    }

    private WebDriver localChromeBrowser() {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        return driver;
    }
}
