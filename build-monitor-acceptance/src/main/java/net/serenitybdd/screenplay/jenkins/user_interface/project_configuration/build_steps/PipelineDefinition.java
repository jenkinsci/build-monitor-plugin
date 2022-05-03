package net.serenitybdd.screenplay.jenkins.user_interface.project_configuration.build_steps;

import net.serenitybdd.screenplay.targets.Target;

public class PipelineDefinition {
    public static final Target Editor = Target.the("code editor").locatedBy("(//div[@id='workflow-editor-1']//textarea)[last()]");
}
