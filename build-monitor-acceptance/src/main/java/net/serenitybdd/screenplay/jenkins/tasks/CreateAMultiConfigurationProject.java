package net.serenitybdd.screenplay.jenkins.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.AddAxis;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.NewJobPage;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateAMultiConfigurationProject implements Task {

    public static CreateAMultiConfigurationProject called(String name) {
        return instrumented(CreateAMultiConfigurationProject.class, name);
    }

    public CreateAMultiConfigurationProject andConfigureItTo(Task configurationTask) {
        this.configureTheProject.add(configurationTask);

        return this;
    }

    @Step("{0} creates a 'Multi-Configuration' called '#name'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAProject.called(name)
                    .ofType(NewJobPage.Multi_Configuration_Project)
                    .andConfigureItTo(configureTheProject)
        );
    }

    public CreateAMultiConfigurationProject(String jobName) {
        this.name = jobName;
    }

    private final String name;
    private final TodoList configureTheProject = TodoList.empty();

}
