package net.serenitybdd.screenplay.jenkins.user_interface.navigation;

import net.serenitybdd.screenplay.targets.Target;

public class Breadcrumbs {

    public static Target linkTo(String name) {
        return Target.the(String.format("the '%s' breadcrumb link", name))
                .locatedBy("//li[@class='jenkins-breadcrumbs__list-item' and a[text()='{0}']]/a")
                .of(name);
    }
}
