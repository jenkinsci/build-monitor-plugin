package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.jenkins.targets.Checkbox;
import net.serenitybdd.screenplay.targets.Target;

public class ProjectConfigurationPage {
    public static final Target Execute_Concurrent_Builds    = Checkbox.withLabel("Execute concurrent builds if necessary");
    public static final Target Add_Build_Step               = Button.called("Add build step");
    public static final Target Add_Post_Build_Action        = Button.called("Add post-build action");
    public static final Target Add_Axis                     = Button.called("Add axis");
}
