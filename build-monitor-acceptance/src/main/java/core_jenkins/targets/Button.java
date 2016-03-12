package core_jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Button {
    public static Target called(String text) {
        return Target.the(format("the '%s' button", text))
                .locatedBy("//button[contains(.,'{0}')]")
                .of(text);
    }
}