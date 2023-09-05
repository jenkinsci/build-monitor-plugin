package net.serenitybdd.screenplay.jenkins.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.user_interface.JenkinsHomePage;

public class ScheduleABuild implements Task {
    public static Task of(String project) {
        return instrumented(ScheduleABuild.class, project);
    }

    @Override
    @Step("{0} schedules a build of the '#project' project  ")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(JenkinsHomePage.Schedule_A_Build.of(project))
                // todo: should wait for the build to finish
                );
    }

    public ScheduleABuild(String project) {
        this.project = project;
    }

    private final String project;
}
