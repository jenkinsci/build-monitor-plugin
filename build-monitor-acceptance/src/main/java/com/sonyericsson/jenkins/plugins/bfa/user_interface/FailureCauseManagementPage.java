package com.sonyericsson.jenkins.plugins.bfa.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Input;
import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class FailureCauseManagementPage {
    public static final Target Create_New_Link = Link.called("Create new");
    public static final Target Name            = Input.named("_.name");
    public static final Target Description     = Target.the("the description textarea").locatedBy("//textarea[@name='_.description']");
    public static final Target Add_Indication  = Button.called("Add Indication");

    public static final Target Build_Log_Indication_Link            = Link.called("Build Log Indication");
    public static final Target Pattern_Field = Input.named("pattern");

    public static final Target Save = Button.called("Save");
}
