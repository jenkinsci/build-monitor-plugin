package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectInformation;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Attribute;
import net.serenitybdd.screenplay.targets.Target;

@Subject("the widget representing the '#projectName' project on the Build Monitor")
public class ProjectWidgetInformation implements Question<ProjectInformation> {

    @Override
    public ProjectInformation answeredBy(Actor actor) {
        Target widget     = BuildMonitorDashboard.Project_Widget.of(projectName);
        String cssClasses = Attribute.of(widget).named("class").answeredBy(actor);

        return new ProjectInformation(projectName, ProjectStatus.fromMultiple(cssClasses));
    }

    public ProjectWidgetInformation(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
