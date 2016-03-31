package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

@Subject("the Build Monitor Widget details representing the '#projectName' project")
public class ProjectWidgetDetails implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        Target details    = BuildMonitorDashboard.Project_Widget_Details.of(projectName);

        return Text.of(details).viewedBy(actor).resolve();
    }

    public ProjectWidgetDetails(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
