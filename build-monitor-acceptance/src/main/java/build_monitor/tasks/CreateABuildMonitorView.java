package build_monitor.tasks;

import build_monitor.tasks.configuration.SaveTheChanges;
import build_monitor.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateABuildMonitorView implements Task {
    public static CreateABuildMonitorView called(String name) {
        return instrumented(CreateABuildMonitorView.class, name);
    }

    public Task andConfigureItTo(Task configurationTask) {
        this.configureBuildMonitor.add(configurationTask);

        return this;
    }

    @Override
    @Step("{0} creates a 'Build Monitor View' called '#buildMonitorName'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            AddABuildMonitorView.called(buildMonitorName),
            configureBuildMonitor,
            SaveTheChanges.andExitTheConfigurationScreen()
        );
    }

    public CreateABuildMonitorView(String name) {
        this.buildMonitorName = name;
    }

    private final String   buildMonitorName;
    private final TodoList configureBuildMonitor = TodoList.empty();
}
