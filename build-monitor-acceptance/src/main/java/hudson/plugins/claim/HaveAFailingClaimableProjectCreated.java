package hudson.plugins.claim;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import hudson.plugins.claim.tasks.configuration.BrokenBuildClaiming;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.HaveAProjectCreated;
import net.serenitybdd.screenplay.jenkins.tasks.ScheduleABuild;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ExecuteAShellScript;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.ShellScriptThat;
import net.thucydides.core.annotations.Step;

public class HaveAFailingClaimableProjectCreated implements Task {
    public static Task called(String name) {
        return instrumented(HaveAFailingClaimableProjectCreated.class, name);
    }

    @Step("{0} creates the '#projectName' project and schedules a build that fails, but can be claimed")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                HaveAProjectCreated.called(projectName).andConfiguredTo(
                        ExecuteAShellScript.that(ShellScriptThat.Finishes_With_Error),
                        BrokenBuildClaiming.allow()
                ),
                ScheduleABuild.of(projectName)
        );
    }

    public HaveAFailingClaimableProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
