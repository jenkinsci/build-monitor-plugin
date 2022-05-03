package net.serenitybdd.screenplayx.actions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

import java.net.URL;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Navigate implements Interaction {

    private final URL destination;

    public static Navigate to(URL destination) {
        return instrumented(Navigate.class, destination);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        BrowseTheWeb.as(actor).getDriver().get(destination.toString());
    }

    public Navigate(URL destination) {
        this.destination = destination;
    }
}
