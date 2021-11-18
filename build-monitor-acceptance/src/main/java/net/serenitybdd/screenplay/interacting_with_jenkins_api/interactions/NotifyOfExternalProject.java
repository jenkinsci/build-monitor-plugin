package net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions;

import net.serenitybdd.screenplay.Action;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class NotifyOfExternalProject {

    public static Action successOf(String project) {
        return instrumented(NotifyOfExternalProjectSuccess.class, project);
    }

    public static Action failureOf(String project) {
        return instrumented(NotifyOfExternalProjectFailure.class, project);
    }
}
