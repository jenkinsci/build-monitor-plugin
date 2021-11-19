package com.sonyericsson.jenkins.plugins.bfa;

import com.sonyericsson.jenkins.plugins.bfa.user_interface.JenkinsHomePageWithBFA;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.TodoList;
import net.serenitybdd.screenplay.jenkins.user_interface.navigation.Breadcrumbs;
import net.thucydides.core.annotations.Step;

import java.util.List;

import static java.util.Arrays.asList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UseFailureCauseManagement implements Task {
    public static UseFailureCauseManagement to(Task... defineFailureCauses) {
        return instrumented(UseFailureCauseManagement.class, asList(defineFailureCauses));
    }

    @Step("{0} uses the 'Failure Cause Management'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(JenkinsHomePageWithBFA.Failure_Cause_Management_Link),
                defineFailureCauses,
                Click.on(Breadcrumbs.Jenkins_Link)
        );
    }

    private final TodoList defineFailureCauses = TodoList.empty();

    public UseFailureCauseManagement(List<Task> defineFailureCauses) {
        this.defineFailureCauses.addAll(defineFailureCauses);
    }
}
