package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.installation.BuildMonitorBuildProperties;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.BuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.buildmonitor.Job;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.scenarios.Scenario;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.utils.Typograph;
import hudson.tasks.Builder;
import hudson.tasks.Shell;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.jvnet.hudson.test.JenkinsRule;
import org.openqa.selenium.By;
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

abstract public class AcceptanceTest implements SauceOnDemandSessionIdProvider {
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    protected WebDriver browser;

    private String sessionId;

    protected Scenario given;

    @Rule
    public final JenkinsRule jenkins = new JenkinsRule();

    @Rule
    public TestName testName = new TestName();

    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    public final BuildMonitorBuildProperties buildProperties = new BuildMonitorBuildProperties("build-monitor.properties");


    @Before
    public void setUp() throws Exception {
        browser = localOrRemoteBrowser();

        browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        given = Scenario.using(jenkins, browser);
    }

    // fixme: an ugly work around with even uglier side effects, needed until the migration from SauceLabs to BrowserStack is finished
    private WebDriver localOrRemoteBrowser() throws MalformedURLException {
        if (shouldUseRemoteSauceLabsBrowser()) {
            sessionId = (((RemoteWebDriver) browser).getSessionId()).toString();
            return sauceLabsBrowser();
        } else if (shouldUseRemoteBrowserStackBrowser()) {
            sessionId = null;               // so that SauceOnDemandTestWatcher doesn't try to report back to SauceLabs
            return browserStackBrowser();
        } else {
            sessionId = null;               // so that SauceOnDemandTestWatcher doesn't try to report back to SauceLabs
            return localChromeBrowser();
        }
    }

    @After
    public void tearDown() throws Exception {
        browser.quit();
    }


    protected BuildMonitor buildMonitorView(String name) throws IOException {
        browser.get(urlFor("view/" + name));

        return new BuildMonitor(browser.findElement(By.className("build-monitor")));
    }

    protected Builder aPassingShellScript() {
        return new Shell("exit 0");
    }

    protected Builder aFailingShellScript() {
        return new Shell("exit 1");
    }

    protected String urlFor(String path) throws IOException {
        return jenkins.getURL().toString() + path;
    }

    private boolean shouldUseRemoteBrowserStackBrowser() {
        return isNotBlank(readPropertyOrEnv("BROWSERSTACK_USERNAME", "")) && isNotBlank(readPropertyOrEnv("BROWSERSTACK_AUTOMATION_KEY", ""));
    }

    private boolean shouldUseRemoteSauceLabsBrowser() {
        return isNotBlank(authentication.getUsername()) && isNotBlank(authentication.getAccessKey());
    }

    private WebDriver browserStackBrowser() throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser", "Chrome");
        capabilities.setCapability("browser_version", "43.0");
        capabilities.setCapability("os", "Windows");
        capabilities.setCapability("os_version", "7");
        capabilities.setCapability("resolution", "1024x768");

        capabilities.setCapability("name",        fullTestName());

        capabilities.setCapability("browserstack.local", "true");

        capabilities.setCapability("build",   currentVersion());
        capabilities.setCapability("project", "Build Monitor");

        return new RemoteWebDriver(
                new URL(String.format("http://%s:%s@%s/wd/hub",
                        readPropertyOrEnv("BROWSERSTACK_USERNAME", ""),
                        readPropertyOrEnv("BROWSERSTACK_AUTOMATION_KEY", ""),
                        "hub.browserstack.com")
                ),
                capabilities
        );
    }

    private String currentVersion() {
        return buildProperties.get("version");
    }

    private WebDriver sauceLabsBrowser() throws MalformedURLException {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("platform", Platform.MAC);

        capabilities.setCapability("version",     readPropertyOrEnv("SELENIUM_VERSION", "43"));
        capabilities.setCapability("platform",    readPropertyOrEnv("SELENIUM_PLATFORM", "mac"));
        capabilities.setCapability("browserName", readPropertyOrEnv("SELENIUM_BROWSER", BrowserType.CHROME));

        capabilities.setCapability("name",        fullTestName());

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

        return driver;
    }

    private String fullTestName() {
        return String.format("%s: %s",
                Typograph.deCamelCase(this.getClass().getSimpleName()),
                Typograph.de_snake_case(testName.getMethodName())
        );
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

    @Override
    public String getSessionId() {
        return sessionId;
    }

    protected Matcher<? super Job> isSuccessful() {
        return new TypeSafeDiagnosingMatcher<Job>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("status should be 'successful'");
            }

            @Override
            protected boolean matchesSafely(final Job job, final Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(job.status());
                return "successful".equalsIgnoreCase(job.status());
            }
        };
    }

    protected Matcher<? super Job> isFailing() {
        return new TypeSafeDiagnosingMatcher<Job>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("status should be 'failing'");
            }

            @Override
            protected boolean matchesSafely(final Job job, final Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(job.status());
                return "failing".equalsIgnoreCase(job.status());
            }
        };
    }

    protected Matcher<? super Job> isDisplayed() {
        return new TypeSafeDiagnosingMatcher<Job>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("should be displayed");
            }

            @Override
            protected boolean matchesSafely(final Job job, final Description mismatchDescription) {
                return null != job;
            }
        };
    }
}