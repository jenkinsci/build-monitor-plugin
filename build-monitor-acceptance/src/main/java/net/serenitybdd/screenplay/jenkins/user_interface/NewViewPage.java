package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

@DefaultUrl("/newView")
public class NewViewPage extends PageObject {
    public static final Target View_Name = Target.the("View name").locatedBy("//*[@id='name']");
    public static final Target Build_Monitor_View = Target.the("the 'Build Monitor View' label")
            .locatedBy("//label[@for='com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView']");
}
