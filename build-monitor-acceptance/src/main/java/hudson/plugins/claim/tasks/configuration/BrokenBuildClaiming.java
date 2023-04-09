package hudson.plugins.claim.tasks.configuration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.AddAPostBuildAction;
import net.thucydides.core.annotations.Step;

public class BrokenBuildClaiming implements Task {

    public static Task allow() {
        return instrumented(BrokenBuildClaiming.class);
    }

    @Step("{0} allows for a broken build to be claimed")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(AddAPostBuildAction.called("Allow broken build claiming"));
    }
}
