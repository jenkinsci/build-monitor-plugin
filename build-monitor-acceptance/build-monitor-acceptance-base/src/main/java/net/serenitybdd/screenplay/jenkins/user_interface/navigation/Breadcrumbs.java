package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Breadcrumbs {
    // Breadcrumb has been renamed Dashboard in newer Jenkins
    public static final Target Jenkins_Link = Target.the("the 'Jenkins' link").locatedBy("//a[./text()='Jenkins' or ./text()='Dashboard']");

    public static Target linkTo(String name) {
        return Target.the(format("the '%s' breadcrumb link", name))
                .locatedBy("//ul[@id='breadcrumbs']//a[contains(., '{0}')]")
                .of(name);
    }
}
