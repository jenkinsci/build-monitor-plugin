package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.targets.Target;

public class ProjectConfigurationPage {
    private ProjectConfigurationPage(){}
    public static final Target Add_Build_Step = Button.called("Add build step");
    public static final Target Add_Post_Build_Action = Button.called("Add post-build action");
}
