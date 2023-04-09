package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

public class Button {
    public static Target called(String text) {
        return Target.the(String.format("the '%s' button", text))
                .locatedBy("//button[contains(.,'{0}')]")
                .of(text);
    }
}
