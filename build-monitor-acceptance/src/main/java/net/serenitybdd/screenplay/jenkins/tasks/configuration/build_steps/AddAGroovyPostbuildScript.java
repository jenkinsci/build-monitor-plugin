package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.user_interface.project_configuration.build_steps.GroovyPostBuildStep;
import net.thucydides.core.annotations.Step;

public class AddAGroovyPostbuildScript implements Task {

    public static Task that(GroovyScript expectedOutcome) {
        return instrumented(AddAGroovyPostbuildScript.class, expectedOutcome);
    }

    @Step("{0} configures the Groovy PostBuild Step to execute a script that '#scriptOutcome'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AddAPostBuildAction.called("Groovy Postbuild"),
                Enter.theValue(scriptOutcome.code()).into(GroovyPostBuildStep.Editor));
    }

    public AddAGroovyPostbuildScript(GroovyScript script) {
        this.scriptOutcome = script;
    }

    private final GroovyScript scriptOutcome;
}
