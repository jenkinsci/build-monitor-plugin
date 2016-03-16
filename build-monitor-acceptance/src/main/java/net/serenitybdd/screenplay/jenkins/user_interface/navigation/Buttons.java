package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.jenkins.targets.Button;

public class Buttons {
    public static final Target Add_Build_Step = Button.called("Add build step");

    public static final Target Save = Button.called("Save");
    public static final Target OK   = Button.called("OK");
}