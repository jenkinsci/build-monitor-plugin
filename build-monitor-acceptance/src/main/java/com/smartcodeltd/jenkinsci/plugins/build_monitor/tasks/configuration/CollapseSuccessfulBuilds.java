package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CollapseSuccessfulBuilds implements Task {

    public static Task usingCollapseSuccessfulBuildsCheckbox() {
        return instrumented(CollapseSuccessfulBuilds.class);
    }

    @Step("{0} clicks the Collapse Successful Builds checkbox")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Choose.the(ViewConfigurationPage.Apply_Collapse_Successful_Builds)
        );
    }
}
