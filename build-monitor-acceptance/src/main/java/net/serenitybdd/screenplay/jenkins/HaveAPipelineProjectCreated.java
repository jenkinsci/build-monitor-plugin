package net.serenitybdd.screenplay.jenkins;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.Sleep;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.CreateAPipelineProject;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.SidePanel;

import java.util.concurrent.TimeUnit;

public class HaveAPipelineProjectCreated implements Task {

    public static HaveAPipelineProjectCreated called(String name) {
        return instrumented(HaveAPipelineProjectCreated.class, name);
    }

    public Task andConfiguredTo(Task... configurationTasks) {
        this.requiredConfiguration.addAll(configurationTasks);

        return this;
    }

    @Step("{0} creates the '#projectName' pipeline")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                CreateAPipelineProject.called(projectName).andConfigureItTo(requiredConfiguration),
                Sleep.of(1, TimeUnit.SECONDS),
                Click.on(SidePanel.Back_to_Dashboard));
    }

    public HaveAPipelineProjectCreated(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
    private final TodoList requiredConfiguration = TodoList.empty();
}
