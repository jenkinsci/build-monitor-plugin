package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

public class ShouldSupportCloudBeesFoldersTest {

    //    Actor anna = Actor.named("Anna");
    //
    //    @Before
    //    public void actorCanBrowseTheWeb() {
    //        anna.can(BrowseTheWeb.with(browser));
    //    }
    //
    //    @Test
    //    public void visualising_projects_nested_in_folders() {
    //
    //        givenThat(anna)
    //                .wasAbleTo(
    //                        Navigate.to(jenkins.url()),
    //                        HaveAFolderCreated.called("Search Services")
    //                                .andInsideIt(
    //                                        HaveANestedProjectCreated.called("Librarian"),
    //                                        HaveAFolderCreated.called("Contracts")
    //                                                .andInsideIt(HaveANestedProjectCreated.called("Third Party
    // System"))),
    //                        Navigate.to(jenkins.url()));
    //
    //        when(anna)
    //                .attemptsTo(CreateABuildMonitorView.called("Build Monitor")
    //                        .andConfigureItTo(
    //                                DisplayAllProjects.usingARegularExpression(),
    // DisplayNestedProjects.fromSubfolders()));
    //
    //        then(anna)
    //                .should(seeThat(
    //                        ProjectWidget.of("Search Services » Librarian").state(),
    // WebElementStateMatchers.isVisible()));
    //        then(anna)
    //                .should(seeThat(
    //                        ProjectWidget.of("Search Services » Contracts » Third Party System")
    //                                .state(),
    //                        WebElementStateMatchers.isVisible()));
    //    }
}
