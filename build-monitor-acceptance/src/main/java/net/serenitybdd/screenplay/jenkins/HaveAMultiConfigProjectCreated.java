package net.serenitybdd.screenplay.jenkins;


import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAMultiConfigurationProject;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel.Back_to_Dashboard;

public class HaveAMultiConfigProjectCreated implements Task{

    public static HaveAMultiConfigProjectCreated called(String name) {
        return instrumented(HaveAMultiConfigProjectCreated.class, name);
    }

    public Task andConfigureItTo(Task... configurationTasks) {
        this.requiredConfiguration.addAll(configurationTasks);

        return this;
    }

    @Step("{0} creates the '#projectName' pipeline")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAMultiConfigurationProject.called(projectName)
                    .andConfigureItTo(requiredConfiguration),
                    Click.on(Back_to_Dashboard)
        );
    }

    public HaveAMultiConfigProjectCreated (String projectName) {
        this.projectName = projectName;
    }


    private final String projectName;
    private final TodoList requiredConfiguration = TodoList.empty();
}
