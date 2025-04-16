package com.cloudbees.hudson.plugins.folder.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.NewJobPage;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Buttons;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel;
import net.serenitybdd.screenplayx.actions.Scroll;
import org.openqa.selenium.Keys;

public class CreateAFolder implements Task {
    public static CreateAFolder called(String name) {
        return instrumented(CreateAFolder.class, name);
    }

    @Override
    @Step("{0} creates a '#name' folder")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(SidePanel.New_Item_Link),
                Enter.theValue(name).into(NewJobPage.Item_Name_Field),
                Scroll.to(NewJobPage.Folder),
                Choose.the(NewJobPage.Folder),
                Click.on(Buttons.OK),
                Click.on(Buttons.Save));
    }

    public CreateAFolder(String jobName) {
        this.name = jobName;
    }

    private final String name;
}
