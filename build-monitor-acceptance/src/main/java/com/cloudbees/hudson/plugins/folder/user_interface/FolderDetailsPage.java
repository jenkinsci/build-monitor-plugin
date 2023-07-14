package com.cloudbees.hudson.plugins.folder.user_interface;

import net.serenitybdd.screenplay.targets.Target;

public class FolderDetailsPage {
    public static final Target Up_Link =
            Target.the("the 'Up' link").locatedBy("(//li[@class='jenkins-breadcrumbs__list-item'])[last()-1]/a");
}
