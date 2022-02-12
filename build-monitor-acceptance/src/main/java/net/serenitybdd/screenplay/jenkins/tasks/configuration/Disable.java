package net.serenitybdd.screenplay.jenkins.tasks.configuration;

import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps.DisableExecutingConcurrentBuilds;

public class Disable {
    public static Task executingConcurrentBuilds() {
        return new DisableExecutingConcurrentBuilds();
    }
}
