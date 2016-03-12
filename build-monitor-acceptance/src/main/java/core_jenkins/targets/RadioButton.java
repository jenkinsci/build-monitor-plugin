package core_jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class RadioButton {
    public static Target withLabel(String text) {
        return Target.the(format("the '%s' radio button", text))
                .locatedBy("//*[label[contains(.,'{0}')]]/input[@type='radio']")
                .of(text);
    }
}