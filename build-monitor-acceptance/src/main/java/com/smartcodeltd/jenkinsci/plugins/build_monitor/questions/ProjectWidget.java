package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectInformation;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectBadgesState;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectWidgetBuilds;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectWidgetDetails;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectWidgetInformation;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectWidgetPipelineStages;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectWidgetProgressBarsState;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget.ProjectWidgetState;
import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.Question;

public class ProjectWidget {

    public static ProjectWidget of(String projectOfInterest) {
        return new ProjectWidget(projectOfInterest);
    }

    public Question<String> executedBuilds() {
        return new ProjectWidgetBuilds(projectOfInterest);
    }

    public Question<ProjectInformation> information() {
        return new ProjectWidgetInformation(projectOfInterest);
    }

    public Question<WebElementState> state() {
        return new ProjectWidgetState(projectOfInterest);
    }

    public Question<String> details() {
        return new ProjectWidgetDetails(projectOfInterest);
    }

    public Question<WebElementState> badges() {
        return new ProjectBadgesState(projectOfInterest);
    }

    public Question<String> pipelineStages() {
        return new ProjectWidgetPipelineStages(projectOfInterest);
    }

    public ProjectWidget(String projectOfInterest) {
        this.projectOfInterest = projectOfInterest;
    }

    public Question<WebElementState> testProgressBars() {
        return new ProjectWidgetProgressBarsState(projectOfInterest);
    }

    private final String projectOfInterest;
}
