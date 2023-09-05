package net.serenitybdd.screenplay.jenkins.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.NewJobPage;

public class CreateAPipelineProject implements Task {
    public static CreateAPipelineProject called(String name) {
        return instrumented(CreateAPipelineProject.class, name);
    }

    public CreateAPipelineProject andConfigureItTo(Task configurationTask) {
        this.configureTheProject.add(configurationTask);

        return this;
    }

    @Override
    @Step("{0} creates a 'Pipeline Project' called '#name'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAProject.called(name).ofType(NewJobPage.Pipeline).andConfigureItTo(this.configureTheProject));
    }

    public CreateAPipelineProject(String jobName) {
        this.name = jobName;
    }

    private final String name;
    private final TodoList configureTheProject = TodoList.empty();
}
