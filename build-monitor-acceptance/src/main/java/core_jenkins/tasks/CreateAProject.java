package core_jenkins.tasks;

import core_jenkins.tasks.recipes.CreateAProjectAndScheduleABuildThatFails;
import core_jenkins.tasks.recipes.CreateAProjectAndScheduleABuildThatPasses;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateAProject {
    public static CreateAProject called(String name) {
        return new CreateAProject(name);
    }

    public Task andScheduleABuildThatPasses() {
        return instrumented(CreateAProjectAndScheduleABuildThatPasses.class, name);
    }

    public Task andScheduleABuildThatFails() {
        return instrumented(CreateAProjectAndScheduleABuildThatFails.class, name);
    }

    public CreateAProject(String name) {
        this.name = name;
    }

    private final String name;
}
