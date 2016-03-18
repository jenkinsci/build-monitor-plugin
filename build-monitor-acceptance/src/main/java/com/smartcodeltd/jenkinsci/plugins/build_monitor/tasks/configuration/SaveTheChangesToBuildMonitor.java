package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Buttons;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SaveTheChangesToBuildMonitor implements Task {
    public static Task andExitTheConfigurationScreen() {
        return instrumented(SaveTheChangesToBuildMonitor.class);
    }

    @Step("{0} saves the changes and leaves the configuration screen")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(Buttons.OK));
    }
}
