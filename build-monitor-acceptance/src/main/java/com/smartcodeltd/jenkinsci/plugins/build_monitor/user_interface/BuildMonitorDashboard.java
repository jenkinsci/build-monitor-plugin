package com.smartcodeltd.jenkinsci.plugins.build_monitor.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class BuildMonitorDashboard {
    public static final Target Add_Some_Projects_link = Link.called("add some projects");
    public static final Target Project_Widget                 = Target.the("Project Widget").locatedBy("//li[header/h2[.='{0}']]");
    public static final Target Project_Widget_Builds          = Target.the("Project Widget Builds").locatedBy("//li[header/h2[.='{0}']]//*[contains(@class, 'build-number')]");
    public static final Target Project_Widget_Details         = Target.the("Project Widget Details").locatedBy("//li[header/h2[.='{0}']]//*[@class='details']");
    public static final Target Project_Widget_Badges          = Target.the("Project Widget Badges").locatedBy("//li[header/h2[.='{0}']]//*[@class='badges']");
    public static final Target Project_Widget_Pipeline_Stages = Target.the("Project Widget Builds").locatedBy("//li[header/h2[.='{0}']]//*[contains(@class, 'build-stages')]");

    public static final Target Control_Panel = Target.the("Control Panel").locatedBy("//label[@for='settings-toggle']");
    public static final Target Show_Badges = Target.the("Show Badges Toggle").locatedBy("//input[@id='settings-show-badges']");
}
