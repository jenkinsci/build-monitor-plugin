package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DisplayNestedProjects implements Task {
    public static Task fromSubfolders() {
        return instrumented(DisplayNestedProjects.class);
    }

    @Step("{0} indicates that the view should include projects nested in subfolders")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Choose.the(ViewConfigurationPage.Recurse_In_Subfolders)
        );
    }
}
