package net.serenitybdd.screenplay.jenkins.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Breadcrumbs;

public class GoBack implements Task {
    public static Task to(String target) {
        return instrumented(GoBack.class, target);
    }

    @Step("{0} navigates back to '#target'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(Breadcrumbs.linkTo(target)));
    }

    private final String target;

    public GoBack(String target) {
        this.target = target;
    }
}
