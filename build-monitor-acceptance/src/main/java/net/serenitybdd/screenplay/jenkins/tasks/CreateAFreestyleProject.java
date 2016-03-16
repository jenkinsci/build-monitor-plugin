package net.serenitybdd.screenplay.jenkins.tasks;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.user_interface.NewJobPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static org.openqa.selenium.Keys.ENTER;
import static net.serenitybdd.screenplay.jenkins.user_interface.navigation.Buttons.Save;
import static net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel.Back_to_Dashboard;

public class CreateAFreestyleProject implements Task {
    public static CreateAFreestyleProject called(String name) {
        return instrumented(CreateAFreestyleProject.class, name);
    }

    public Task andConfigureItTo(Task configurationTask) {
        this.configureTheProject.add(configurationTask);

        return this;
    }

    @Override
    @Step("{0} creates a 'Freestyle Project' called '#name'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(SidePanel.New_Item_Link),
                Choose.the(NewJobPage.Freestyle_Project),
                Enter.theValue(name).into(NewJobPage.Item_Name_Field).thenHit(ENTER),
                configureTheProject,
                Click.on(Save),
                Click.on(Back_to_Dashboard)
        );
    }

    public CreateAFreestyleProject(String jobName) {
        this.name = jobName;
    }

    private final String   name;
    private final TodoList configureTheProject = TodoList.empty();
}