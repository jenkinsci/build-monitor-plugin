package core_jenkins.tasks.recipes;

import core_jenkins.tasks.CreateAFreestyleProject;
import core_jenkins.tasks.ScheduleABuild;
import core_jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import core_jenkins.tasks.configuration.build_steps.ShellScript;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

public class CreateAProjectAndScheduleABuildThatPasses implements Task {

    @Step("{0} creates the '#projectName' project and schedules a build that passes")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAFreestyleProject.called(projectName).andConfigureItTo(
                        ExecuteAShellScript.that(ShellScript.Finishes_with_Success)
                ),
                ScheduleABuild.of(projectName)
        );
    }

    public CreateAProjectAndScheduleABuildThatPasses(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
