package net.serenitybdd.screenplay.jenkins.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.jenkins.JenkinsUser;
import net.serenitybdd.screenplay.jenkins.user_interface.JenkinsHomePage;
import net.serenitybdd.screenplay.jenkins.user_interface.LogInForm;

public class LogIn implements Task {
    public static LogIn as(JenkinsUser actor) {
        return instrumented(LogIn.class, actor);
    }

    @Step("{0} logs in")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(JenkinsHomePage.Log_In_Link),
                Enter.theValue(actor.getName()).into(LogInForm.Username_Field),
                Enter.theValue(passwordOf(actor)).into(LogInForm.Password_Field),
                Click.on(LogInForm.Log_In_Buttton));
    }

    private String passwordOf(Actor actor) {
        return ((JenkinsUser) actor).password();
    }

    private final JenkinsUser user;

    public LogIn(JenkinsUser actor) {
        this.user = actor;
    }
}
