package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldTellWhoIsFixingTheBrokenBuildTest {

    @Test
    void test(Page p, JenkinsRule j) {
        //        givenThat(ben)
        //                .wasAbleTo(
        //                        Navigate.to(jenkins.url()),
        //                        LogIn.as(ben),
        //                        HaveAFailingClaimableProjectCreated.called("Responsibly Developed"));
        //
        //        when(ben)
        //                .attemptsTo(
        //                        HaveABuildMonitorViewCreated.showingAllTheProjects(),
        //                        Claim.lastBrokenBuildOf("Responsibly Developed").saying("My bad, fixing now"),
        //                        GoBack.to("Build Monitor"));
        //
        //        then(ben)
        //                .should(seeThat(
        //                        ProjectWidget.of("Responsibly Developed").information(),
        //                        ProjectInformationMatchers.displaysProjectStatusAs(ProjectStatus.Claimed)));
        //        then(ben)
        //                .should(seeThat(
        //                        ProjectWidget.of("Responsibly Developed").details(), is("Claimed by Ben: My bad,
        // fixing
        // now")));
    }
}
