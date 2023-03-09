package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

public class Input {
    public static Target named(String name) {
        return Target.the(String.format("the '%s' input", name))
                .locatedBy("//input[@name='{0}']")
                .of(name);
    }
}
