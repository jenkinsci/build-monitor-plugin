package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.EnterCode;
import net.serenitybdd.screenplay.jenkins.user_interface.project_configuration.build_steps.PipelineDefinition;

public class SetPipelineDefinition implements Task {

    public static Task asFollows(String pipelineDefintion) {
        return instrumented(SetPipelineDefinition.class, pipelineDefintion);
    }

    @Step("{0} configures the Pipeline Defintion to execute '#pipelineDefintion'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(EnterCode.asFollows(pipelineDefintion).intoThePipelineEditor(PipelineDefinition.Editor));
    }

    public SetPipelineDefinition(String pipelineDefintion) {
        this.pipelineDefintion = pipelineDefintion;
    }

    private final String pipelineDefintion;
}
