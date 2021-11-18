package features;

import environment.JenkinsSandbox;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SerenityParameterizedRunner.class)
public abstract class BuildMonitorAbstractBase {

    @Managed public WebDriver browser;

    @Rule public JenkinsInstance jenkins = JenkinsSandbox.configure().afterStart(jenkinsAfterStartRules()).create();
    
    protected abstract List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules();


    public static Collection<Object[]> testData(){
        List<Object[]> data = new ArrayList<>();
        
        String[] jenkinsVersions = System.getenv("JENKINS_VERSIONS").split(",");
        for (String jenkinsVersion : jenkinsVersions) {
            data.add(new Object[]{jenkinsVersion});
        }

        return data;
    }
    
    protected BuildMonitorAbstractBase(String jenkinsVersion) {
        System.setProperty("jenkins.version", jenkinsVersion);
    }
}
