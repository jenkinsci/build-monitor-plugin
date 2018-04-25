package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.SaveTheChangesToBuildMonitor;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import java.util.List;

import static java.util.Arrays.asList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConfigureEmptyBuildMonitorView implements Task {

    public static ConfigureEmptyBuildMonitorView to(Task... configurationTasks) {
        return instrumented(ConfigureEmptyBuildMonitorView.class, asList(configurationTasks));
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
