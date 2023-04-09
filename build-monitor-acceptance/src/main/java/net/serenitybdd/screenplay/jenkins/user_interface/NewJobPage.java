package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("/newJob")
public class NewJobPage extends PageObject {
    public static final Target Item_Name_Field = Target.the("Item name").locatedBy("//*[@id='name']");
    public static final Target Freestyle_Project = Target.the("Freestyle project")
            .locatedBy(
                    "//*[@id=\"j-add-item-type-standalone-projects\"]/ul/li[label/input[@value='hudson.model.FreeStyleProject']]");
    public static final Target Pipeline = Target.the("Pipeline")
            .locatedBy(
                    "//*[@id=\"j-add-item-type-standalone-projects\"]/ul/li[label/input[@value='org.jenkinsci.plugins.workflow.job.WorkflowJob']]");
    public static final Target Folder = Target.the("Folder")
            .locatedBy(
                    "//*[@id=\"j-add-item-type-nested-projects\"]/ul/li[label/input[@value='com.cloudbees.hudson.plugins.folder.Folder']]");
    public static final Target External_Project = Target.the("External Job")
            .locatedBy(
                    "//*[@id=\"j-add-item-type-standalone-projects\"]/ul/li[label/input[@value='hudson.model.ExternalJob']]");
}
