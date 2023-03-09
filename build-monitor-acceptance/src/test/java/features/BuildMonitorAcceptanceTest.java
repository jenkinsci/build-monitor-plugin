package features;

import environment.JenkinsSandbox;
import java.util.List;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public abstract class BuildMonitorAcceptanceTest {

    @Managed public WebDriver browser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure()
            .beforeStart(jenkinsBeforeStartRules())
            .afterStart(jenkinsAfterStartRules())
            .create();
    
    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsBeforeStartRules() {
        return List.of();
    }
    
    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
        return List.of();
    }
}
