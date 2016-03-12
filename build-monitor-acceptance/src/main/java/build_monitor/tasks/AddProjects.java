package build_monitor.tasks;

import build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AddProjects implements Task {
    public static Task toAnEmptyBuildMonitor() {
        return instrumented(AddProjects.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(BuildMonitorDashboard.Add_Some_Projects_link)
        );
    }
}
