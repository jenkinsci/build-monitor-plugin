package net.serenitybdd.screenplay.jenkins;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAFreestyleProject;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScript;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel.Back_to_Dashboard;

public class HaveASuccessfulProjectCreated implements Task {

    public static Task called(String name) {
        return instrumented(HaveASuccessfulProjectCreated.class, name);
    }

    @Step("{0} creates the '#projectName' project and schedules a build that passes")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAFreestyleProject.called(projectName).andConfigureItTo(
                        ExecuteAShellScript.that(ShellScript.Finishes_with_Success)
                ),
                Click.on(Back_to_Dashboard),
                ScheduleABuild.of(projectName)
        );
    }

    public HaveASuccessfulProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
