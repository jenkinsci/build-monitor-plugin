package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

class ShouldTellWhoIsFixingTheBrokenBuildTest {

    //    JenkinsUser ben;
    //
    //    @Override
    //    protected List<? extends ApplicativeTestRule<JenkinsInstance>> jenkinsAfterStartRules() {
    //        ben = JenkinsUser.named("Ben");
    //        return List.of(RegisterUserAccount.of(ben));
    //    }
    //
    //    @Before
    //    public void actorCanBrowseTheWeb() {
    //        ben.can(BrowseTheWeb.with(browser));
    //    }
    //
    //    @Test
    //    public void claiming_a_broken_build() {
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
    //                        ProjectWidget.of("Responsibly Developed").details(), is("Claimed by Ben: My bad, fixing
    // now")));
    //    }
}
