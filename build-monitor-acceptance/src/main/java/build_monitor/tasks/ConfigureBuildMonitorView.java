package build_monitor.tasks;

import build_monitor.tasks.configuration.SaveTheChanges;
import build_monitor.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import java.util.List;

import static java.util.Arrays.asList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConfigureBuildMonitorView implements Task {

    public static ConfigureBuildMonitorView to(Task... configurationTasks) {
        return instrumented(ConfigureBuildMonitorView.class, asList(configurationTasks));
    }

    @Override
    @Step("{0} configures the Build Monitor View")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AddProjects.toAnEmptyBuildMonitor(),
                configureTheView,
                SaveTheChanges.andExitTheConfigurationScreen()
        );
    }

    public ConfigureBuildMonitorView(List<Performable> actions) {
        this.configureTheView.addAll(actions);
    }

    private TodoList configureTheView = TodoList.empty();
}
