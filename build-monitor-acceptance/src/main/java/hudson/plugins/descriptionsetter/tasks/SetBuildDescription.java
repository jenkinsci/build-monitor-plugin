package hudson.plugins.descriptionsetter.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.targets.Setting;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.AddABuildStep;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SetBuildDescription implements Task {

    private final String description;
    private String regex = "";

    public static SetBuildDescription to(String description) {
        return instrumented(SetBuildDescription.class, description);
    }

    public SetBuildDescription basedOnLogLineMatching(String regex) {
        this.regex = regex;

        return this;
    }

    public SetBuildDescription(String description) {
        this.description = description;
    }

    @Step("{0} indicates that the build description should be set to '#description', based on regex '#regex')")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AddABuildStep.called("Set build description"),
                Enter.theValue(regex).into(Setting.defining("Regular expression")),
                Enter.theValue(description).into(Setting.defining("Description"))
        );
    }
}
