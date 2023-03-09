package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.EnterCode;
import net.serenitybdd.screenplay.jenkins.user_interface.project_configuration.build_steps.ShellBuildStep;
import net.thucydides.core.annotations.Step;

public class ExecuteAShellScript implements Task {

    public static Task that(ShellScript expectedOutcome) {
        return instrumented(ExecuteAShellScript.class, expectedOutcome);
    }

    @Step("{0} configures the Shell Step to execute a script that '#scriptOutcome'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            AddABuildStep.called("Execute shell"),
            EnterCode.asFollows(scriptOutcome.code()).intoTheCodeMirror(ShellBuildStep.Editor)
        );
    }

    public ExecuteAShellScript(ShellScript script) {
        this.scriptOutcome = script;
    }

    private final ShellScript scriptOutcome;
}
