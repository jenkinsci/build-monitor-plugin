package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.jenkins.targets.Label;
import net.serenitybdd.screenplay.targets.Target;

@DefaultUrl("/newJob")
public class NewJobPage extends PageObject {
    public static final Target Item_Name_Field = Target.the("Item name").locatedBy("//*[@id='name']");
    public static final Target Freestyle_Project = Label.called("Freestyle project");
    public static final Target Pipeline = Label.called("Pipeline");
    public static final Target Folder = Label.called("Folder");
    public static final Target External_Project = Label.called("External Job");
}
