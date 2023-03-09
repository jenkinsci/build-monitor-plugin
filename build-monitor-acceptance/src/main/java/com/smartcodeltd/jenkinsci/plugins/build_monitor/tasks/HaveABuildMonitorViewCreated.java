package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

public class HaveABuildMonitorViewCreated implements Task {
    public static Task showingAllTheProjects() {
        return instrumented(HaveABuildMonitorViewCreated.class);
    }

    @Step("{0} creates a 'Build Monitor View' showing all the projects")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                DisplayAllProjects.usingARegularExpression()
        ));
    }
}