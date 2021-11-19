package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Link {
    public static Target called(String text) {
        return to(text);
    }

    public static Target to(String text) {
        return Target.the(format("the '%s' link", text))
                .locatedBy("//a[contains(., '{0}')]")
                .of(text);
    }
}