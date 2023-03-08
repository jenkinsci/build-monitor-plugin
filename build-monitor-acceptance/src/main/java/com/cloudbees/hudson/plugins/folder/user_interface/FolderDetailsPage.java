package com.cloudbees.hudson.plugins.folder.user_interface;

import net.serenitybdd.screenplay.targets.Target;

public class FolderDetailsPage {
    public static final Target Up_Link = Target.the("the 'Up' link").locatedBy("//a[contains(., 'Up')]//..");
}
