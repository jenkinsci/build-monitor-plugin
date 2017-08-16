package com.smartcodeltd.jenkinsci.plugins.build_monitor.questions.project_widget;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface.BuildMonitorDashboard;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

public class ProjectMultiConfigWidget implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        Target multiConfigBadge1    = BuildMonitorDashboard.Multi_Config_1;
        Target multiConfigBadge2    = BuildMonitorDashboard.Multi_Config_2;

        return Text.of(multiConfigBadge1).viewedBy(actor).resolve() + ":::" + Text.of(multiConfigBadge2).viewedBy(actor).resolve();
    }
}
