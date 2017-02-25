package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.user_interface.ProjectConfigurationPage;
import net.thucydides.core.annotations.Step;

public class EnableExecutingConcurrentBuilds implements Task {

    @Step("{0} enables executing concurrent builds")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(ProjectConfigurationPage.Execute_Concurrent_Builds)
        );
    }
}
