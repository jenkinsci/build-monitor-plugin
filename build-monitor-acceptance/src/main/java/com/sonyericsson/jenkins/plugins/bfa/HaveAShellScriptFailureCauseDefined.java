package com.sonyericsson.jenkins.plugins.bfa;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

public class HaveAShellScriptFailureCauseDefined implements Task {

    public static HaveAShellScriptFailureCauseDefined called(String name) {
        return instrumented(HaveAShellScriptFailureCauseDefined.class, name);
    }

    public HaveAShellScriptFailureCauseDefined describedAs(String description) {
        this.description = description;

        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(UseFailureCauseManagement.to(DefineABuildLogIndicatedFailureCause.called(name)
                .describedAs(description)
                .matching(Build_Log_Pattern)));
    }

    public HaveAShellScriptFailureCauseDefined(String name) {
        this.name = name;
    }

    private final String name;
    private String description = "A shell script has failed";

    private static final String Build_Log_Pattern = "Build step 'Execute shell' marked build as failure";
}
