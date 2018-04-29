package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.questions.WebElementQuestion.stateOf;

@Subject("partial badge")
public class CollapsePartial implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        Target partialCollapse    = BuildMonitorDashboard.Collapse_Partial;

        return Text.of(partialCollapse).viewedBy(actor).resolve();
    }
}