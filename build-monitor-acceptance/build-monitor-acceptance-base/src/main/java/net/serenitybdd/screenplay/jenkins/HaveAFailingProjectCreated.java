package net.serenitybdd.screenplay.jenkins;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScriptThat.Finishes_With_Error;

public class HaveAFailingProjectCreated implements Task {

    public static Task called(String name) {
        return instrumented(HaveAFailingProjectCreated.class, name);
    }

    @Step("{0} creates the '#projectName' project and schedules a build that fails")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                HaveAProjectCreated.called(projectName).andConfiguredTo(
                        ExecuteAShellScript.that(Finishes_With_Error)
                ),
                ScheduleABuild.of(projectName)
        );
    }

    public HaveAFailingProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
