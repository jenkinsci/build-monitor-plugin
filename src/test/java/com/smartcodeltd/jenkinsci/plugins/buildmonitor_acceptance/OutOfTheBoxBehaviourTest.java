package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.pageobjects.BuildMonitor;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.recipes.With;
import org.junit.Ignore;
import org.junit.Test;
import org.jvnet.hudson.test.recipes.LocalData;
import org.openqa.selenium.By;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OutOfTheBoxBehaviourTest extends AcceptanceTest {

    @Test
    @LocalData
    @With(plugins = { "git-1.5.0.hpi", "git-client-1.8.0.jpi", "ssh-credentials-1.6.1.jpi", "credentials-1.10.jpi" })
    public void correctly_displays_successful_and_failing_jobs() throws Exception {
//      Given 'example-build' job finished with 'success'
//        and 'example-acceptance' job finished with 'failure'
//       Then the Build Monitor presents the following:
//         * 'example-build' job is displayed as 'successful'
//         * 'acceptance' job is displayed as 'failed'

        givenFollowingProjectRunHasSucceeded("example-build");
        givenFollowingProjectRunHasFailed("example-acceptance");

        browser.get(urlFor("view/Build Monitor"));
        BuildMonitor bm = new BuildMonitor(browser.findElement(By.className("build-monitor")));

        assertThat(bm.job("example-build").status(), is("successful"));
        assertThat(bm.job("example-acceptance").status(), is("failing"));
    }
}