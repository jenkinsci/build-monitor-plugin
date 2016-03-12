package build_monitor.tasks.configuration;

import build_monitor.user_interface.BuildMonitorViewConfigurationPage;
import core_jenkins.actions.Choose;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DisplayAllProjects implements Task {
    public static Task usingARegularExpression() {
        return instrumented(DisplayAllProjects.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Choose.the(BuildMonitorViewConfigurationPage.Use_Regular_Expression),
            Enter.theValue(".*").into(BuildMonitorViewConfigurationPage.Regular_Expression)
        );
    }
}
