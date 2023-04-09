package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class SidePanel {
    public static final Target New_Item_Link = Link.to("New Item");
    public static final Target Back_to_Dashboard =
            Target.the("Back to Dashboard").locatedBy("//*[@id=\"breadcrumbs\"]//a[@href='/']");
}
