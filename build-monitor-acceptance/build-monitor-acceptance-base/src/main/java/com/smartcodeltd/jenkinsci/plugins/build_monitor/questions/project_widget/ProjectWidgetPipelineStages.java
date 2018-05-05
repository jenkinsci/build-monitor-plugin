package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

@Subject("the pipeline stages of the widget representing the '#projectName' project on the Build Monitor")
public class ProjectWidgetPipelineStages implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        Target details = BuildMonitorDashboard.Project_Widget_Pipeline_Stages.of(projectName);

        return Text.of(details).viewedBy(actor).resolve();
    }

    public ProjectWidgetPipelineStages(String projectName) {
        this.projectName = projectName;
    }

    private final String projectName;
}
