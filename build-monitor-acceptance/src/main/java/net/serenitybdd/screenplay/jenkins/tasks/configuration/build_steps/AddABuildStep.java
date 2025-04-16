package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.Sleep;
import java.util.concurrent.TimeUnit;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.jenkins.user_interface.ProjectConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;

public class AddABuildStep implements Task {
    public static Task called(String buildStepName) {
        return new AddABuildStep(buildStepName);
    }

    @Step("{0} adds the '#buildStepName' build step")
    @Override
    public <T extends Actor> void performAs(final T actor) {
        actor.attemptsTo(
                Sleep.of(1, TimeUnit.SECONDS),
                Scroll.to(ProjectConfigurationPage.Add_Build_Step),
                Click.on(ProjectConfigurationPage.Add_Build_Step),
                Click.on(Button.called(buildStepName)));
    }

    public AddABuildStep(String buildStepName) {
        this.buildStepName = buildStepName;
    }

    private final String buildStepName;
}
