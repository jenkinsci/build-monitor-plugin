package features;

import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.util.Collections;
import java.util.List;

@RunWith(SerenityRunner.class)
public abstract class BuilMonitorAcceptanceTest {

    @Managed public WebDriver browser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure()
            .beforeStart(jenkinsBeforeStartRules())
            .afterStart(jenkinsAfterStartRules())
            .create();
    
    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsBeforeStartRules() {
        return Collections.emptyList();
    }
    
    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
        return Collections.emptyList();
    }
}
