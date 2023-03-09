package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.Sleep;
import java.util.concurrent.TimeUnit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

public class ConfigureViewSettings implements Task {

    public static Task toggleShowBadges() {
        return instrumented(ConfigureViewSettings.class, ViewConfigurationPage.Enable_Show_Badges);
    }

    @Step("{0} configures the view settings")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Sleep.of(1, TimeUnit.SECONDS),
                Scroll.to(target),
                Sleep.of(1, TimeUnit.SECONDS),
                Click.on(target));
    }

    public ConfigureViewSettings(Target target) {
        this.target = target;
    }

    private final Target target;
}
