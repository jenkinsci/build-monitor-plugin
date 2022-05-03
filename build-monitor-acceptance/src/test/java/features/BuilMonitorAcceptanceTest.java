package features;

import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(SerenityParameterizedRunner.class)
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


    public static Collection<Object[]> testData(){
        List<Object[]> data = new ArrayList<>();
        
        String[] jenkinsVersions = System.getenv("JENKINS_VERSIONS").split(",");
        for (String jenkinsVersion : jenkinsVersions) {
            data.add(new Object[]{jenkinsVersion});
        }

        return data;
    }
    
    protected BuilMonitorAcceptanceTest(String jenkinsVersion) {
        System.setProperty("jenkins.version", jenkinsVersion);
    }
    
    protected Path getpluginsCache() {
        return Paths.get(System.getenv("PLUGINS_CACHE"));
    }
}
