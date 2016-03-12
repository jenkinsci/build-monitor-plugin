package build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;
import user_interface.navigation.Buttons;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SaveTheChanges implements Task {
    public static Task andExitTheConfigurationScreen() {
        return instrumented(SaveTheChanges.class);
    }

    @Step("{0] saves the changes")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(Buttons.OK));
    }
}
