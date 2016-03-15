package core_jenkins.user_interface;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;
import core_jenkins.targets.RadioButton;

@DefaultUrl("/newJob")
public class NewJobPage extends PageObject {
    public static final Target Item_Name_Field   = Target.the("the 'Item name' field").locatedBy("#name");
    public static final Target Freestyle_Project = RadioButton.withLabel("Freestyle project");
}