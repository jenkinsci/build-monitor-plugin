package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.jenkins.targets.Label;
import net.serenitybdd.screenplay.targets.Target;

public class ProjectConfigurationPage {
    public static final Target Execute_Concurrent_Builds = Label.called("Execute concurrent builds if necessary");
    public static final Target Do_Not_Allow_Concurrent_Builds = Label.called("Do not allow concurrent builds");
    public static final Target Add_Build_Step = Button.called("Add build step");
    public static final Target Add_Post_Build_Action = Button.called("Add post-build action");
}
