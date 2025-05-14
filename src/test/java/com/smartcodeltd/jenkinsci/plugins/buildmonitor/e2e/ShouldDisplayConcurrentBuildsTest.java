package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

class ShouldDisplayConcurrentBuildsTest {

    //    private static String My_App = "My App";
    //
    //    Actor dave = Actor.named("Dave");
    //
    //    @Before
    //    public void actorCanBrowseTheWeb() {
    //        dave.can(BrowseTheWeb.with(browser));
    //    }
    //
    //    @Test
    //    public void displaying_concurrent_builds() {
    //        givenThat(dave)
    //                .wasAbleTo(
    //                        Navigate.to(jenkins.url()),
    //                        HaveAProjectCreated.called(My_App)
    //                                .andConfiguredTo(
    //                                        Enable.executingConcurrentBuilds(),
    // ExecuteAShellScript.that(sleepsFor(300))),
    //                        ScheduleABuild.of(My_App),
    //                        Navigate.to(jenkins.url()),
    //                        ScheduleABuild.of(My_App));
    //
    //        when(dave).attemptsTo(HaveABuildMonitorViewCreated.showingAllTheProjects());
    //
    //        then(dave).should(seeThat(ProjectWidget.of(My_App).executedBuilds(), is("#2\n#1")));
    //    }
    //
    //    private ShellScript sleepsFor(int seconds) {
    //        return ShellScript.that("simulates executing a build").definedAs(String.format("sleep %d;", seconds));
    //    }
}
