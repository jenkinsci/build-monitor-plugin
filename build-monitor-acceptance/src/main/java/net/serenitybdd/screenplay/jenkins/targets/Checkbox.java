package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Checkbox {
    private Checkbox(){}
    public static Target withLabel(String text) {
        return Target.the(format("the '%s' checkbox", text))
                .locatedBy("//*[label[contains(.,'{0}')]]/input[@type='checkbox']")
                .of(text);
    }
}