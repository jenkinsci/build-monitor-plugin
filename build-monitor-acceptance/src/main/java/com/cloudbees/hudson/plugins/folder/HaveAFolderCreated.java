package com.cloudbees.hudson.plugins.folder;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.cloudbees.hudson.plugins.folder.tasks.CreateAFolder;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;

public class HaveAFolderCreated implements Task {

    public static HaveAFolderCreated called(String name) {
        return instrumented(HaveAFolderCreated.class, name);
    }

    public Task andInsideIt(Task... createProjects) {
        configurationTasks.addAll(createProjects);

        return this;
    }

    @Step("{0} creates and configures the '#name' folder")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(CreateAFolder.called(name), configurationTasks);
    }

    public HaveAFolderCreated(String name) {
        this.name = name;
    }

    private final String name;
    private final TodoList configurationTasks = TodoList.empty();
}
