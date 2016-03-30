package net.serenitybdd.screenplay.jenkins.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.jenkins.user_interface.JenkinsHomePage;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;

import java.net.URL;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Start implements Task {

    public static Start withJenkinsAt(URL url) {
        Injectors.getInjector().getInstance(EnvironmentVariables.class).setProperty("webdriver.base.url", url.toString());

        return instrumented(Start.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.browserOn(homePage)
        );
    }

    JenkinsHomePage homePage;
}