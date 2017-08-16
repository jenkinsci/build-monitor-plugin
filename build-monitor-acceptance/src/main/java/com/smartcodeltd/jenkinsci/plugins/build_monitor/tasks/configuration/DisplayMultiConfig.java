package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DisplayMultiConfig implements Task{

    public static Task usingDisplayMultiConfigCheckbox() {
        return instrumented(DisplayMultiConfig.class);
    }

    @Step("{0} clicks the Display Multi-Config Jobs checkbox")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Choose.the(ViewConfigurationPage.Display_Multi_Config_Jobs)
        );
    }
}
