package net.serenitybdd.screenplay.interacting_with_jenkins_api.exceptions;

public class ActorCannotInteractWithJenkinsApi extends RuntimeException {
    public ActorCannotInteractWithJenkinsApi(String actorName) {
        super("The actor " + actorName + " does not have the ability to interact with Jenkins via the API");
    }
}
