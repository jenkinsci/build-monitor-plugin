package com.cloudbees.hudson.plugins.folder;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.cloudbees.hudson.plugins.folder.user_interface.FolderDetailsPage;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.Sleep;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAFreestyleProject;

import java.util.concurrent.TimeUnit;

public class HaveANestedProjectCreated implements Task {

    public static Task called(String name) {
        return instrumented(HaveANestedProjectCreated.class, name);
    }

    @Step("{0} creates the '#projectName' project")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(CreateAFreestyleProject.called(projectName), Sleep.of(1, TimeUnit.SECONDS), Click.on(FolderDetailsPage.Up_Link));
    }

    public HaveANestedProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
