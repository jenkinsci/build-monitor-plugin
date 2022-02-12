package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.ViewConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DisplayJunitRealtimeProgress implements Task {
    public static Task bars() {
        return instrumented(DisplayJunitRealtimeProgress.class);
    }

    @Step("{0} indicates that the view should display junit realtime tests progress")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Scroll.to(ViewConfigurationPage.Display_JUnit_Realtime_Progress),
            Choose.the(ViewConfigurationPage.Display_JUnit_Realtime_Progress)
        );
    }
}
