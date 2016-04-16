package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class JenkinsHomePage {
    public static final Target New_View_link    = Link.called("+");
    public static final Target Schedule_A_Build = Target.the("the 'build now' link").locatedBy("//table[@id='projectstatus']//tr[td/a[.='{0}']]/td/a[contains(@href, '/build')]");
    public static final Target Log_In_Link      = Link.called("log in");
}