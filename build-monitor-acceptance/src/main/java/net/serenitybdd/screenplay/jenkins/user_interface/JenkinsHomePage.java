package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.targets.Target;

public class JenkinsHomePage {
    public static final Target New_View_link = Target.the("the 'New View' link").locatedBy("//a[@title='New View']");
    public static final Target Schedule_A_Build =
            Target.the("the 'build now' link").locatedBy("//a[@title='Schedule a Build for {0}']");
    public static final Target Log_In_Link = Target.the("sign in link")
            .locatedBy("//a[normalize-space(text())='log in' or normalize-space(text())='Sign in']");
}
