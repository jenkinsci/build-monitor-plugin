package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Item;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldSupportMultibranchPipelinesTest {

    @Test
    void test(Page p, JenkinsRule j) throws Exception {
        // Create a multibranch pipeline project
        WorkflowMultiBranchProject multibranch = j.createProject(WorkflowMultiBranchProject.class, "MyMultibranch");

        // Create branch jobs and add them to the multibranch project
        WorkflowJob branch1 = new WorkflowJob(multibranch, "master");
        branch1.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        // Add branch via reflection (addLoadedChild is protected in AbstractFolder)
        java.lang.reflect.Method addMethod = multibranch.getClass().getSuperclass().getSuperclass().getDeclaredMethod("addLoadedChild", Item.class);
        addMethod.setAccessible(true);
        addMethod.invoke(multibranch, branch1);

        WorkflowJob branch2 = new WorkflowJob(multibranch, "develop");
        branch2.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        addMethod.invoke(multibranch, branch2);

        // Save the projects to persist state
        multibranch.save();
        branch1.save();
        branch2.save();

        // Create a BuildMonitorView that includes the multibranch project (without recursion)
        var view = createBuildMonitorView(j, "Build Monitor").addJobs(multibranch);

        // Verify that the branch jobs appear in the monitor view
        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJob("MyMultibranch / master")
                .hasJob("MyMultibranch / develop");
    }
}
