package net.serenitybdd.screenplay.jenkins.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.actions.Choose;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.NewJobPage;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.jenkins.user_interface.navigation.Buttons.Save;
import static org.openqa.selenium.Keys.ENTER;

class CreateAProject implements Task {
    public static CreateAProject called(String name) {
        return instrumented(CreateAProject.class, name);
    }

    public CreateAProject ofType(Target projectType) {
        this.projectType = projectType;

        return this;
    }

    public CreateAProject andConfigureItTo(Task configurationTask) {
        this.configureTheProject.add(configurationTask);

        return this;
    }

    @Override
    @Step("{0} creates a 'Freestyle Project' called '#name'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(SidePanel.New_Item_Link),
                Choose.the(projectType),
                Enter.theValue(name).into(NewJobPage.Item_Name_Field).thenHit(ENTER),
                configureTheProject,
                Click.on(Save)
        );
    }

    public CreateAProject(String jobName) {
        this.name = jobName;
    }

    private final String    name;
    private final TodoList  configureTheProject = TodoList.empty();
    private       Target    projectType;
}