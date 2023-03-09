package net.serenitybdd.screenplay.jenkins;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAFreestyleProject;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel;
import net.thucydides.core.annotations.Step;

public class HaveAProjectCreated implements Task {

    public static HaveAProjectCreated called(String name) {
        return instrumented(HaveAProjectCreated.class, name);
    }

    public Task andConfiguredTo(Task... configurationTasks) {
        this.requiredConfiguration.addAll(configurationTasks);

        return this;
    }

    @Step("{0} creates the '#projectName' project")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAFreestyleProject.called(projectName).andConfigureItTo(requiredConfiguration),
                Click.on(SidePanel.Back_to_Dashboard)
        );
    }

    public HaveAProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
    private final TodoList requiredConfiguration = TodoList.empty();
}
