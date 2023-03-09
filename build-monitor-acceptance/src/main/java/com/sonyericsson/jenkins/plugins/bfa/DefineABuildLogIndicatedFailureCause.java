package com.sonyericsson.jenkins.plugins.bfa;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.sonyericsson.jenkins.plugins.bfa.tasks.DefineAFailureCause;
import com.sonyericsson.jenkins.plugins.bfa.tasks.configuration.LineInTheBuildLog;
import net.serenitybdd.integration.utils.Nulls;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

public class DefineABuildLogIndicatedFailureCause implements Task {

    public static DefineABuildLogIndicatedFailureCause called(String name) {
        return instrumented(DefineABuildLogIndicatedFailureCause.class, name);
    }

    public DefineABuildLogIndicatedFailureCause describedAs(String description) {
        this.description = description;

        return this;
    }

    public DefineABuildLogIndicatedFailureCause matching(String regex) {
        this.regex = regex;

        return this;
    }

    @Step("{0} defines what constitutes a problem with '#name'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                DefineAFailureCause.called(name).describedAs(Nulls.getOrElse(description, name)).indicatedBy(
                        LineInTheBuildLog.matching(regex)
                )
        );
    }

    public DefineABuildLogIndicatedFailureCause(String name) {
        this.name = name;
    }

    private String name        = "Shell Script Failure";
    private String regex       = "Build step 'Execute shell' marked build as failure";

    private String description;
}
