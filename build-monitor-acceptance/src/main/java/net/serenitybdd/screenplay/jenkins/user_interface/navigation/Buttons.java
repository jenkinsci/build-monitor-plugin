package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.targets.Target;

public class Buttons {
    public static final Target Create = Target.the("the 'Create' button").locatedBy("//input[@name='Submit' and @value='Create']");
    public static final Target Save = Button.called("Save");
    public static final Target OK   = Button.called("OK");
}