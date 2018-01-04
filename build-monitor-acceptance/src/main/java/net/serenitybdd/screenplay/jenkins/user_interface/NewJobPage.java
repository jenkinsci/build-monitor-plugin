package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Setting;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.jenkins.targets.RadioButton;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("/newJob")
public class NewJobPage extends PageObject {
    public static final Target Item_Name_Field   = Setting.defining("Item name");
    public static final Target Freestyle_Project = RadioButton.withLabel("Freestyle project");
    public static final Target Pipeline          = RadioButton.withLabel("Pipeline");
    public static final Target External_Project  = RadioButton.withLabel("External Job");
}