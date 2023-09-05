package net.serenitybdd.screenplay.interacting_with_jenkins_api.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.abilities.InteractWithJenkinsAPI;

class NotifyOfExternalProjectSuccess implements Interaction {
    @Override
    @Step("{0} notifies Jenkins that the '#project' has succeeded")
    public <T extends Actor> void performAs(T actor) {
        InteractWithJenkinsAPI.as(actor).notifyOfExternalProjectSuccessOf(project);
    }

    public NotifyOfExternalProjectSuccess(String project) {
        this.project = project;
    }

    private final String project;
}
