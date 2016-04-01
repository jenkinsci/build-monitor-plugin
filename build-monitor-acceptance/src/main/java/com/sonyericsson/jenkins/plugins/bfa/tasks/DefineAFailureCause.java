package com.sonyericsson.jenkins.plugins.bfa.tasks;

import com.sonyericsson.jenkins.plugins.bfa.user_interface.FailureCauseManagementPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DefineAFailureCause implements Task {
    public static DefineAFailureCause called(String name) {
        return instrumented(DefineAFailureCause.class, name);
    }

    public DefineAFailureCause describedAs(String description) {
        this.description = description;

        return this;
    }

    public DefineAFailureCause indicatedBy(Task... configurationTasks) {
        configureFailureCauseIndicators.addAll(configurationTasks);

        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(FailureCauseManagementPage.Create_New_Link),
                Enter.theValue(name).into(FailureCauseManagementPage.Name),
                Enter.theValue(description).into(FailureCauseManagementPage.Description),
                configureFailureCauseIndicators,
                Click.on(FailureCauseManagementPage.Save)
        );
    }

    private final String name;
    private String description;
    private final TodoList configureFailureCauseIndicators = TodoList.empty();

    public DefineAFailureCause(String name) {
        this.name = name;
    }
}
