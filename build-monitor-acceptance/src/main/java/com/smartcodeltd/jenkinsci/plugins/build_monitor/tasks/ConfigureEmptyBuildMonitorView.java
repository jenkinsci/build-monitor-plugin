package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.SaveTheChangesToBuildMonitor;
import java.util.Arrays;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.thucydides.core.annotations.Step;

public class ConfigureEmptyBuildMonitorView implements Task {

    public static ConfigureEmptyBuildMonitorView to(Task... configurationTasks) {
        return instrumented(ConfigureEmptyBuildMonitorView.class, Arrays.asList(configurationTasks));
    }

    @Override
    @Step("{0} configures the Build Monitor View")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                configureTheView,
                SaveTheChangesToBuildMonitor.andExitTheConfigurationScreen()
        );
    }

    public ConfigureEmptyBuildMonitorView(List<Performable> actions) {
        this.configureTheView.addAll(actions);
    }

    private TodoList configureTheView = TodoList.empty();
}
