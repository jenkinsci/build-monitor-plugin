package net.serenitybdd.integration.browserstack;

import net.serenitybdd.readability.Typograph;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static java.lang.String.*;

public class BrowserStackTestSessionName extends TestWatcher {

    private final String projectName;
    private       String build = "";

    public BrowserStackTestSessionName(String projectName) {
        this.projectName = projectName;
    }

    public static BrowserStackTestSessionName forProject(String projectName){
        return new BrowserStackTestSessionName(projectName);
    }

    public BrowserStackTestSessionName andBuild(String build) {
        this.build = build;

        return this;
    }

    @Override
    protected void starting(Description description) {
        // fixme: this seems a bit hacky, but that's the best we can do before improving the SerenityTestRunner
        EnvironmentVariables props = Injectors.getInjector().getInstance(EnvironmentVariables.class);

        props.setProperty("browserstack.name",    humanReadable(description));
        props.setProperty("browserstack.project", projectName);

        if (! "".equalsIgnoreCase(build)) {
            props.setProperty("browserstack.build",   "");
        }
    }

    private String humanReadable(Description description) {
        return format("%s: %s",
                Typograph.deCamelCase(simplified(description.getClassName())),
                Typograph.de_snake_case(description.getMethodName())
        );
    }

    private String simplified(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }
}
