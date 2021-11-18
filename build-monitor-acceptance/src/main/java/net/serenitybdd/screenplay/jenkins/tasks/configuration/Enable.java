package net.serenitybdd.screenplay.jenkins.tasks.configuration;

import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.EnableExecutingConcurrentBuilds;

public class Enable {
    public static Task executingConcurrentBuilds() {
        return new EnableExecutingConcurrentBuilds();
    }
}
