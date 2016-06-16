package com.sonyericsson.jenkins.plugins.bfa.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class JenkinsHomePageWithBFA {

    private JenkinsHomePageWithBFA(){}

    public static final Target Failure_Cause_Management_Link = Link.called("Failure Cause Management");
}
