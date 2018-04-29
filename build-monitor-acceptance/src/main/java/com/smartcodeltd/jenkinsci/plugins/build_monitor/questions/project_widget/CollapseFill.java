package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

@Subject("fullscreen badge")
public class CollapseFill implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        Target fillCollapse    = BuildMonitorDashboard.Collapse_Fill;

        return Text.of(fillCollapse).viewedBy(actor).resolve();
    }
}