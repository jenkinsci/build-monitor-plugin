package com.sonyericsson.jenkins.plugins.bfa;

import com.sonyericsson.jenkins.plugins.bfa.tasks.DefineAFailureCause;
import com.sonyericsson.jenkins.plugins.bfa.tasks.configuration.LineInTheBuildLog;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.integration.utils.Nulls.getOrElse;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DefineAShellScriptFailureCause implements Task {

    public static DefineAShellScriptFailureCause called(String name) {
        return instrumented(DefineAShellScriptFailureCause.class, name);
    }

    public DefineAShellScriptFailureCause describedAs(String description) {
        this.description = description;

        return this;
    }

    public DefineAShellScriptFailureCause matching(String regex) {
        this.regex = regex;

        return this;
    }

    @Step("{0} defines what constitutes a 'Shell Script Failure'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                DefineAFailureCause.called(name).describedAs(getOrElse(description, name)).indicatedBy(
                        LineInTheBuildLog.matching(regex)
                )
        );
    }

    public DefineAShellScriptFailureCause(String name) {
        this.name = name;
    }

    private final String name;
    private String description;
    private String regex       = "Build step 'Execute shell' marked build as failure";
}
