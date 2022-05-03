package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.CheckCheckbox;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.UncheckCheckbox;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConfigureViewSettings implements Task {

    public static Task showBadges() {
        return instrumented(ConfigureViewSettings.class, ViewConfigurationPage.Enable_Show_Badges, true);
    }

    public static Task doNotShowBadges() {
        return instrumented(ConfigureViewSettings.class, ViewConfigurationPage.Enable_Show_Badges, false);
    }

    @Step("{0} configures the view settings")
    @Override
    public <T extends Actor> void performAs(T actor) {
        if (value instanceof Boolean) {
            actor.attemptsTo(
                    Scroll.to(target),
                    ((Boolean) value).booleanValue() ? CheckCheckbox.of(target) : UncheckCheckbox.of(target)
            );
        } else {
            actor.attemptsTo(
                    Scroll.to(target),
                    Enter.theValue(value.toString()).into(target)
            );
        }
    }

    public ConfigureViewSettings(Target target, Object value) {
        this.target = target;
        this.value = value;
    }

    private final Target target;
    private final Object value;
}
