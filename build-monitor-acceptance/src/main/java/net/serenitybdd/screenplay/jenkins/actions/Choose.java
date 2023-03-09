package net.serenitybdd.screenplay.jenkins.actions;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

public class Choose implements Interaction {
    public static Interaction the(Target radioButton) {
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
