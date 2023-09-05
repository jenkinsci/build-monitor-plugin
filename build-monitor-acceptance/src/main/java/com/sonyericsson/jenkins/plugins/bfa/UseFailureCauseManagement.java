package com.sonyericsson.jenkins.plugins.bfa;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import com.sonyericsson.jenkins.plugins.bfa.user_interface.JenkinsHomePageWithBFA;
import java.util.List;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Breadcrumbs;

public class UseFailureCauseManagement implements Task {
    public static UseFailureCauseManagement to(Task... defineFailureCauses) {
        return instrumented(UseFailureCauseManagement.class, List.of(defineFailureCauses));
    }

    @Step("{0} uses the 'Failure Cause Management'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(JenkinsHomePageWithBFA.Failure_Cause_Management_Link),
                defineFailureCauses,
                Click.on(Breadcrumbs.Jenkins_Link));
    }

    private final TodoList defineFailureCauses = TodoList.empty();

    public UseFailureCauseManagement(List<Task> defineFailureCauses) {
        this.defineFailureCauses.addAll(defineFailureCauses);
    }
}
