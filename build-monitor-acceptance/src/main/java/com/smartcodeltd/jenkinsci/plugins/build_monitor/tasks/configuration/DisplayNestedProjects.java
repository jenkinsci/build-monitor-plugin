package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;

public class DisplayNestedProjects implements Task {
    public static Task fromSubfolders() {
        return instrumented(DisplayNestedProjects.class);
    }

    @Step("{0} indicates that the view should include projects nested in subfolders")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(ViewConfigurationPage.Recurse_In_Subfolders),
                Choose.the(ViewConfigurationPage.Recurse_In_Subfolders));
    }
}
