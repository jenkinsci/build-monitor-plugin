package net.serenitybdd.screenplay.jenkins.tasks.configuration;

import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.jenkins.user_interface.ProjectConfigurationPage;
import net.serenitybdd.screenplay.jenkins.user_interface.EditUserDefinedAxis;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

public class AddAxis implements Task {
    public static Task of(Axis axis) {
        return new AddAxis(axis);
    }

    @Step("{0} configures Multi-Configuration project axis")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(ProjectConfigurationPage.Add_Axis),
                Click.on(ProjectConfigurationPage.Add_Axis),
                Click.on(Link.called("User-defined Axis")),
                Enter.theValue(axis.getName()).into(EditUserDefinedAxis.EditAxisName),
                Enter.theValue(axis.getValues()).into(EditUserDefinedAxis.EditAxisVals)
        );
    }

    public AddAxis(Axis axis) {
        this.axis = axis;
    }

    private Axis axis;
}
