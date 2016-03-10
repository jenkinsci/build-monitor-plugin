import org.jenkinsci.test.acceptance.junit.JenkinsAcceptanceTestRule;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BuildMonitorTest {

    @Rule
    public JenkinsAcceptanceTestRule rules = new JenkinsAcceptanceTestRule();

    /**
     * Jenkins under test.
     */
    @Inject
    public Jenkins jenkins;

    private WebDriver browser;

    @Test
    public void should_work() throws Exception {
        browser = browserStackBrowser();

        browser.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        browser.get(jenkins.url("").toString());

        assertThat(browser.getTitle(), is("Dashboard [Jenkins]"));
    }

    @After
    public void tearDown() throws Exception {
        browser.quit();
    }

    private WebDriver browserStackBrowser() throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser", "Chrome");
        capabilities.setCapability("browser_version", "43.0");
        capabilities.setCapability("os", "Windows");
        capabilities.setCapability("os_version", "7");
        capabilities.setCapability("resolution", "1024x768");

        capabilities.setCapability("name",    "browserstack experiments");

        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("browserstack.local", "true");

        capabilities.setCapability("build",   "experimental");
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

    private String readPropertyOrEnv(String key, String defaultValue) {
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
