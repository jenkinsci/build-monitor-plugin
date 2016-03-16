package core_jenkins.tasks.configuration.build_steps;

import core_jenkins.actions.EnterCode;
import core_jenkins.user_interface.project_configuration.build_steps.ShellBuildStep;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

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
