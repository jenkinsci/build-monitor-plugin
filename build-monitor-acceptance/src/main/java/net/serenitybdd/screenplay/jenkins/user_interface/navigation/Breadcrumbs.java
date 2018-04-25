package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Breadcrumbs {
    public static final Target Jenkins_Link = Target.the("the 'Jenkins' link").locatedBy("//a[./text()='Jenkins']");

    public static Target linkTo(String name) {
        return Target.the(format("the '%s' breadcrumb link", name))
                .locatedBy("//ul[@id='breadcrumbs']//a[contains(., '{0}')]")
                .of(name);
    }
}
