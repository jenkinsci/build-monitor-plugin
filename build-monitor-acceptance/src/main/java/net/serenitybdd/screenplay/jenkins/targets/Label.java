package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

public class Label {
    public static Target called(String text) {
        return Target.the(String.format("the '%s' label", text))
                .locatedBy("//label[contains(.,'{0}')]")
                .of(text);
    }
}
