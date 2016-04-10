package hudson.plugins.claim.tasks;

import hudson.plugins.claim.user_interface.ClaimableBuildDetailsPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.jenkins.user_interface.ProjectDetailsPage;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Claim implements Task {
    public static Claim lastBrokenBuildOf(String project) {
        return instrumented(Claim.class, project);
    }

    public Claim saying(String reason) {
        this.reason = reason;

        return this;
    }

    @Step("{0} claims the last broken build of '#project' saying: '#reason'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(Link.called(project)),
            Click.on(ProjectDetailsPage.Last_Failed_Build_Link),
            Click.on(ClaimableBuildDetailsPage.Claim_It_Link),
            Enter.theValue(reason).into(ClaimableBuildDetailsPage.Reason_Field),
            Click.on(ClaimableBuildDetailsPage.Claim_Button)
        );
    }

    private final String project;
    private String reason = "";

    public Claim(String project) {
        this.project = project;
    }
}
