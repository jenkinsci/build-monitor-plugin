package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.questions.WebElementQuestion.stateOf;

@Subject("the Build Monitor Widget representing the '#projectName' project")
public class ProjectWidgetState implements Question<WebElementState> {

    @Override
    public WebElementState answeredBy(Actor actor) {
        Target widget     = BuildMonitorDashboard.Project_Widget.of(projectName);

        return stateOf(widget).answeredBy(actor);
    }

    public ProjectWidgetState(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
