package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.saucelabs.common.SauceOnDemandAuthentication;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

abstract public class AcceptanceTest {
    @Rule
    public final JenkinsRule j = new JenkinsRule();

    @Rule
    public TestName testName = new TestName();

    protected WebDriver browser;

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    @Before
    public void setUp() throws Exception {
        browser = shouldUseRemoteBrowser() ?
                sauceLabsBrowser() :
                localChromeBrowser();
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

    protected boolean shouldUseRemoteBrowser() {
        return StringUtils.isNotBlank(authentication.getUsername()) && StringUtils.isNotBlank(authentication.getAccessKey());
    }

    private WebDriver sauceLabsBrowser() throws MalformedURLException {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("platform", Platform.MAC);

        capabilities.setCapability("version",     readPropertyOrEnv("SELENIUM_VERSION", "34"));
        capabilities.setCapability("platform",    readPropertyOrEnv("SELENIUM_PLATFORM", "mac"));
        capabilities.setCapability("browserName", readPropertyOrEnv("SELENIUM_BROWSER", BrowserType.CHROME));

        // @TODO: this should include both the test class and the method name in a human-readable format
        capabilities.setCapability("name",     testName.getMethodName().replaceAll("_", " "));

        return new RemoteWebDriver(
            new URL(String.format("http://%s:%s@%s:%s/wd/hub",
                    authentication.getUsername(),
                    authentication.getAccessKey(),
                    readPropertyOrEnv("SELENIUM_HOST", "localhost"),
                    readPropertyOrEnv("SELENIUM_PORT", "4445"))
            ),
            capabilities
        );
    }

    private WebDriver localChromeBrowser() {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        return driver;
    }

    public String readPropertyOrEnv(String key, String defaultValue) {
        String v = System.getProperty(key);
        if (v == null) {
            v = System.getenv(key);
        }
        if (v == null) {
            v = defaultValue;
        }

        return v;
    }
}
