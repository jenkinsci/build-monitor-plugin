package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorViewConfigurationPage;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
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
            Choose.the(BuildMonitorViewConfigurationPage.Use_Regular_Expression),
            Enter.theValue(".*").into(BuildMonitorViewConfigurationPage.Regular_Expression)
        );
    }
}
