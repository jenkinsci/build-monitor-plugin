package com.sonyericsson.jenkins.plugins.bfa.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Setting;
import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class FailureCauseManagementPage {
    public static final Target Create_New     = Link.called("Create new");
    public static final Target Name           = Target.the("Name field").locatedBy("//*[@name='_.name']");
    public static final Target Description    = Target.the("Description field").locatedBy("//*[@name='_.description']");
    public static final Target Comment        = Target.the("Comment field").locatedBy("//*[@name='_.comment']");
    public static final Target Add_Indication = Button.called("Add Indication");

    public static final Target Build_Log_Indication_Link            = Link.called("Build Log Indication");
    public static final Target Multi_Line_Build_Log_Indication_Link = Link.called("Multi-Line Build Log Indication");

    public static final Target Pattern_Field = Setting.defining("Pattern");

    public static final Target Save = Button.called("Save");
}
