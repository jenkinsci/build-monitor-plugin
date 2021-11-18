package net.serenitybdd.screenplay.jenkins.actions;

import net.serenitybdd.screenplay.Action;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Choose implements Action{
    public static Action the(Target radioButton) {
        return instrumented(Choose.class, radioButton);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(radioButton));
    }

    public Choose(Target radioButton) {
        this.radioButton = radioButton;
    }

    private final Target radioButton;
}
