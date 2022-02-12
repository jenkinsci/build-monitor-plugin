package net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.abilities.InteractWithJenkinsAPI;
import net.thucydides.core.annotations.Step;

class NotifyOfExternalProjectFailure implements Interaction {
    @Override
    @Step("{0} notifies Jenkins that the '#project' has failed")
    public <T extends Actor> void performAs(T actor) {
        InteractWithJenkinsAPI.as(actor).notifyOfExternalProjectFailureOf(project);
    }

    public NotifyOfExternalProjectFailure(String project) {
        this.project = project;
    }

    private final String project;
}
