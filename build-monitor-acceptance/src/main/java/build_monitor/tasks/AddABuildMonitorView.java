package build_monitor.tasks;

import core_jenkins.user_interface.JenkinsHomePage;
import core_jenkins.user_interface.NewViewPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static org.openqa.selenium.Keys.ENTER;

public class AddABuildMonitorView implements Task {
    public static AddABuildMonitorView called(String name) {
        return instrumented(AddABuildMonitorView.class, name);
    }

    @Override
    @Step("{0} adds a 'Build Monitor View' called '#name'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(JenkinsHomePage.New_View_link),
            Click.on(NewViewPage.Build_Monitor_View),
            Enter.theValue(name).into(NewViewPage.View_Name).thenHit(ENTER)
        );
    }

    public AddABuildMonitorView(String name) {
        this.name = name;
    }

    private final String name;
}
