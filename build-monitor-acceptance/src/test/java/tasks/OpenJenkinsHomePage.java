package tasks;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.annotations.Step;
import ui.JenkinsHomePage;

import java.net.URL;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class OpenJenkinsHomePage implements Task {

    JenkinsHomePage jenkinsHomePage;

    private final URL address;

    public static Task at(URL address) {
        return instrumented(OpenJenkinsHomePage.class, address);
    }

    @Step("Open the application")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.browserOn(specified(jenkinsHomePage))
        );
    }

    private PageObject specified(PageObject page) {
        page.setDefaultBaseUrl(address.toString());

        return page;
    }

    public OpenJenkinsHomePage(URL address) {
        this.address = address;
    }
}
