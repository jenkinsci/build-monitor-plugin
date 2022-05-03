package net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions;

import net.serenitybdd.screenplay.Interaction;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class NotifyOfExternalProject {

    public static Interaction successOf(String project) {
        return instrumented(NotifyOfExternalProjectSuccess.class, project);
    }

    public static Interaction failureOf(String project) {
        return instrumented(NotifyOfExternalProjectFailure.class, project);
    }
}
