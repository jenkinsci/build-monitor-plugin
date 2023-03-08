package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.SaveTheChangesToBuildMonitor;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.JenkinsHomePage;
import net.serenitybdd.screenplay.jenkins.user_interface.NewViewPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Buttons;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateABuildMonitorView implements Task {
    public static CreateABuildMonitorView called(String name) {
        return instrumented(CreateABuildMonitorView.class, name);
    }

    public Task andConfigureItTo(Task... configurationTasks) {
        this.configureBuildMonitor.addAll(configurationTasks);

        return this;
    }

    @Override
    @Step("{0} creates a 'Build Monitor View' called '#buildMonitorName'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(JenkinsHomePage.New_View_link),
                Enter.theValue(buildMonitorName).into(NewViewPage.View_Name),
                Click.on(NewViewPage.Build_Monitor_View),
                Click.on(Buttons.Create),
                configureBuildMonitor,
                SaveTheChangesToBuildMonitor.andExitTheConfigurationScreen()
        );
    }

    public CreateABuildMonitorView(String name) {
        this.buildMonitorName = name;
    }

    private final String   buildMonitorName;
    private final TodoList configureBuildMonitor = TodoList.empty();
}
