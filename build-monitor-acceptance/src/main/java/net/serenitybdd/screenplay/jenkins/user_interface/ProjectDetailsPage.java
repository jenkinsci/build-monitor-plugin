package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.targets.Target;

public class ProjectDetailsPage {
    private ProjectDetailsPage(){}
    public static final Target Last_Failed_Build_Link = Link.called("Last failed build");
}
