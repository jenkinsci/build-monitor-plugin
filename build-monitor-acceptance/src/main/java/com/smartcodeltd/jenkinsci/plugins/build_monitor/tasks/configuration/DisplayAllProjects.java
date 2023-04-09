package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.Sleep;
import java.util.concurrent.TimeUnit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

public class DisplayAllProjects implements Task {
    public static Task usingARegularExpression() {
        return instrumented(DisplayAllProjects.class);
    }

    @Step("{0} uses a regular expression to display all projects")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Sleep.of(1, TimeUnit.SECONDS),
                Scroll.to(ViewConfigurationPage.Use_Regular_Expression),
                Sleep.of(1, TimeUnit.SECONDS),
                Click.on(ViewConfigurationPage.Use_Regular_Expression),
                Enter.theValue(".*").into(ViewConfigurationPage.Regular_Expression));
    }
}
