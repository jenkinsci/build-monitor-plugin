package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.user_interface.ProjectConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

public class DisableExecutingConcurrentBuilds implements Task {

    @Step("{0} disables executing concurrent builds")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(ProjectConfigurationPage.Do_Not_Allow_Concurrent_Builds),
                Click.on(ProjectConfigurationPage.Do_Not_Allow_Concurrent_Builds));
    }
}
