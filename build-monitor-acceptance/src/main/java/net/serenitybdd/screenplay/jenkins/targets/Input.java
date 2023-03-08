package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Input {
    public static Target named(String name) {
        return Target.the(format("the '%s' input", name))
                .locatedBy("//input[@name='{0}']")
                .of(name);
    }
}
