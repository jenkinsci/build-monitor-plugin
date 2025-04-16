package net.serenitybdd.screenplay.jenkins;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAnExternalProject;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel;

public class HaveAnExternalProjectCreated implements Task {

    public static HaveAnExternalProjectCreated called(String name) {
        return instrumented(HaveAnExternalProjectCreated.class, name);
    }

    @Step("{0} creates the '#projectName' external project")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(CreateAnExternalProject.called(projectName), Click.on(SidePanel.Back_to_Dashboard));
    }

    public HaveAnExternalProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
