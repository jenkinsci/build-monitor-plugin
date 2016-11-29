package net.serenitybdd.screenplay.interacting_with_jenkins_api.abilities;

import net.serenitybdd.integration.jenkins.client.JenkinsClient;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.interacting_with_jenkins_api.exceptions.ActorCannotInteractWithJenkinsApi;

public class InteractWithJenkinsAPI implements Ability {

    private final JenkinsClient client;

    public static Ability using(JenkinsClient client) {
        return new InteractWithJenkinsAPI(client);
    }

    public static InteractWithJenkinsAPI as(Actor actor) {
        if(actor.abilityTo(InteractWithJenkinsAPI.class) == null) {
            throw new ActorCannotInteractWithJenkinsApi(actor.getName());
        }

        return actor.abilityTo(InteractWithJenkinsAPI.class);
    }

    public void notifyOfExternalProjectSuccessOf(String project) {
        client.setExternalBuildResult(project, "0");
    }

    public void notifyOfExternalProjectFailureOf(String project) {
        client.setExternalBuildResult(project, "1");
    }

    @Override
    public Ability asActor(Actor actor) {
        return this;
    }

    public InteractWithJenkinsAPI(JenkinsClient client) {
        this.client = client;
    }
}
