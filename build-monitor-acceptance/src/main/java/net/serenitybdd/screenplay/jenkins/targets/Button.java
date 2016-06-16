package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Button {
    private Button(){}
    public static Target called(String text) {
        return Target.the(format("the '%s' button", text))
                .locatedBy("//button[contains(.,'{0}')]")
                .of(text);
    }
}