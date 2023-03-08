package com.sonyericsson.jenkins.plugins.bfa.tasks.configuration;

import com.sonyericsson.jenkins.plugins.bfa.user_interface.FailureCauseManagementPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class LineInTheBuildLog implements Task {
    public static LineInTheBuildLog matching(String pattern) {
        return instrumented(LineInTheBuildLog.class, pattern);
    }

    @Step("{0} defines a pattern indicating the failure cause")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(FailureCauseManagementPage.Add_Indication),
                Click.on(FailureCauseManagementPage.Add_Indication),
                WaitUntil.the(FailureCauseManagementPage.Build_Log_Indication_Link, WebElementStateMatchers.isVisible()),
                Click.on(FailureCauseManagementPage.Build_Log_Indication_Link),
                Enter.theValue(pattern).into(FailureCauseManagementPage.Pattern_Field)
        );
    }

    private final String pattern;

    public LineInTheBuildLog(String pattern) {
        this.pattern = pattern;
    }
}
