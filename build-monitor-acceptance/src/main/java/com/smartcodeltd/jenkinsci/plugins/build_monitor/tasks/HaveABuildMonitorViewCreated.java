package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks.configuration.DisplayAllProjects;
import net.serenitybdd.screenplay.Task;

public class HaveABuildMonitorViewCreated {
    public static Task showingAllTheProjects() {
        return CreateABuildMonitorView.called("Build Monitor").andConfigureItTo(
                DisplayAllProjects.usingARegularExpression()
        );
    }
}