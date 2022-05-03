package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DisplayAllProjects implements Task {
    public static Task usingARegularExpression() {
        return instrumented(DisplayAllProjects.class);
    }

    @Step("{0} uses a regular expression to display all projects")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Scroll.to(ViewConfigurationPage.Use_Regular_Expression),
            Choose.the(ViewConfigurationPage.Use_Regular_Expression),
            Enter.theValue(".*").into(ViewConfigurationPage.Regular_Expression)
        );
    }
}
