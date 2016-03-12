package build_monitor.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import core_jenkins.user_interface.JenkinsHomePage;
import core_jenkins.user_interface.NewViewPage;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static org.openqa.selenium.Keys.ENTER;
import static user_interface.navigation.Buttons.OK;

public class CreateABuildMonitorView implements Task {
    public static CreateABuildMonitorView called(String name) {
        return instrumented(CreateABuildMonitorView.class, name);
    }

    @Override
    @Step("{0} creates a 'Build Monitor View' called '#name'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(JenkinsHomePage.New_View_link),
            Click.on(NewViewPage.Build_Monitor_View),
            Enter.theValue(name).into(NewViewPage.View_Name).thenHit(ENTER),
            Click.on(OK)
        );
    }

    public CreateABuildMonitorView(String name) {
        this.name = name;
    }

    private final String name;
}
