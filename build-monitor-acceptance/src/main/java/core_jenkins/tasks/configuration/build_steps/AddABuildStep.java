package core_jenkins.tasks.configuration.build_steps;

import core_jenkins.targets.Link;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;
import user_interface.navigation.Buttons;

public class AddABuildStep implements Task {
    public static Task called(String buildStepName) {
        return new AddABuildStep(buildStepName);
    }

    @Step("{0} adds the '#buildStepName' build step")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(Buttons.Add_Build_Step),
                Click.on(Buttons.Add_Build_Step),
                Click.on(Link.called(buildStepName))
        );
    }

    public AddABuildStep(String buildStepName) {
        this.buildStepName = buildStepName;
    }

    private final String buildStepName;
}
