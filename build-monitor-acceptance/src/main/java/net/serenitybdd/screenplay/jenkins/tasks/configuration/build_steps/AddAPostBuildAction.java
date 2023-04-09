package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.Sleep;
import java.util.concurrent.TimeUnit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.jenkins.user_interface.ProjectConfigurationPage;
import net.serenitybdd.screenplayx.actions.Scroll;
import net.thucydides.core.annotations.Step;

public class AddAPostBuildAction implements Task {
    public static Task called(String buildStepName) {
        return new AddAPostBuildAction(buildStepName);
    }

    @Step("{0} adds the '#postBuildAction' post-build action")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Sleep.of(1, TimeUnit.SECONDS),
                Scroll.to(ProjectConfigurationPage.Add_Post_Build_Action),
                Sleep.of(1, TimeUnit.SECONDS),
                Click.on(ProjectConfigurationPage.Add_Post_Build_Action),
                Sleep.of(1, TimeUnit.SECONDS),
                Click.on(Link.called(postBuildActionName)));
    }

    public AddAPostBuildAction(String postBuildActionName) {
        this.postBuildActionName = postBuildActionName;
    }

    private final String postBuildActionName;
}
