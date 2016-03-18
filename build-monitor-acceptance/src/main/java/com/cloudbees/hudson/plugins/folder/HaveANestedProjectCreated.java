package com.cloudbees.hudson.plugins.folder;

import com.cloudbees.hudson.plugins.folder.user_interface.FolderDetailsPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAFreestyleProject;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class HaveANestedProjectCreated implements Task {

    public static Task called(String name) {
        return instrumented(HaveANestedProjectCreated.class, name);
    }

    @Step("{0} creates the '#projectName' project")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAFreestyleProject.called(projectName),
                Click.on(FolderDetailsPage.Up_Link)
        );
    }

    public HaveANestedProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
