package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.jenkins.targets.RadioButton;
import net.serenitybdd.screenplay.jenkins.targets.Setting;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("/newView")
public class NewViewPage extends PageObject {
    public static final Target View_Name          = Setting.defining("View name");
    public static final Target Build_Monitor_View = RadioButton.withLabel("Build Monitor View");
}