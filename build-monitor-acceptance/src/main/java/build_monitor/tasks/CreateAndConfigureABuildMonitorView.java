package build_monitor.tasks;

import build_monitor.tasks.configuration.DisplayAllProjects;
import net.serenitybdd.screenplay.Task;

public class CreateAndConfigureABuildMonitorView {

    public static CreateAndConfigureABuildMonitorView called(String name) {
        return new CreateAndConfigureABuildMonitorView(name);
    }

    public Task thatDisplaysAllTheProjects() {
        return CreateABuildMonitorView.called(name).andConfigureItTo(
                DisplayAllProjects.usingARegularExpression()
        );
    }

    public CreateAndConfigureABuildMonitorView(String name) {
        this.name = name;
    }

    private final String name;
}
