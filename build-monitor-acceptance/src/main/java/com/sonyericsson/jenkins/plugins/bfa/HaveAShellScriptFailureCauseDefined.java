package com.sonyericsson.jenkins.plugins.bfa;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class HaveAShellScriptFailureCauseDefined implements Task {

    public static Task called(String name) {
        return instrumented(HaveAShellScriptFailureCauseDefined.class, name);
    }

    @Step("{0} defines what constitutes a '#name' failure")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                UseFailureCauseManagement.to(
                        DefineAShellScriptFailureCause.called(name)
                )
        );
    }

    public HaveAShellScriptFailureCauseDefined(String name) {
        this.name = name;
    }

    private final String name;
}
