package features;

import org.junit.After;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class AbstractTestBase {
    private WebDriver webDriver;

    WebDriver getWebDriver() {
        if (webDriver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--lang=en");
            webDriver = new ChromeDriver(options);
            webDriver.manage().window().setSize(new Dimension(1440, 900));
        }
        return webDriver;
    }

    @After
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
