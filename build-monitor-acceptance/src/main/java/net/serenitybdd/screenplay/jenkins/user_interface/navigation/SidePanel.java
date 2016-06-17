package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.jenkins.targets.Link;

public class SidePanel {
    private SidePanel(){}

    public static final Target New_Item_Link = Link.to("New Item");
    public static final Target Back_to_Dashboard = Link.to("Back to Dashboard");
}