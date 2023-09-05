package net.serenitybdd.screenplay.jenkins.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.NewJobPage;

public class CreateAFreestyleProject implements Task {
    public static CreateAFreestyleProject called(String name) {
        return instrumented(CreateAFreestyleProject.class, name);
    }

    public CreateAFreestyleProject andConfigureItTo(Task configurationTask) {
        this.configureTheProject.add(configurationTask);

        return this;
    }

    @Override
    @Step("{0} creates a 'Freestyle Project' called '#name'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(CreateAProject.called(name)
                .ofType(NewJobPage.Freestyle_Project)
                .andConfigureItTo(this.configureTheProject));
    }

    public CreateAFreestyleProject(String jobName) {
        this.name = jobName;
    }

    private final String name;
    private final TodoList configureTheProject = TodoList.empty();
}
