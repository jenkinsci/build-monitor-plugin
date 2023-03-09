package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import java.util.Arrays;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.thucydides.core.annotations.Step;

public class ModifyControlPanelOptions implements Task {

    public static ModifyControlPanelOptions to(Task... configurationTasks) {
        return instrumented(ModifyControlPanelOptions.class, Arrays.asList(configurationTasks));
    }

    @Override
    @Step("{0} modifies the Build Monitor View control panel options")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(BuildMonitorDashboard.Control_Panel),
                configureTheView,
                Click.on(BuildMonitorDashboard.Control_Panel)
        );
    }

    public ModifyControlPanelOptions(List<Performable> actions) {
        this.configureTheView.addAll(actions);
    }

    private TodoList configureTheView = TodoList.empty();
}
