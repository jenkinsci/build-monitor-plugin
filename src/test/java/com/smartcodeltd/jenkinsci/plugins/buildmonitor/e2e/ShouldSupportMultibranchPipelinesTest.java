package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Item;
import hudson.model.ItemGroup;
import java.lang.reflect.Method;
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

        // Create branch jobs using Jenkins' ItemGroup API
        // WorkflowMultiBranchProject extends AbstractFolder which extends ItemGroup
        // We use getItem(String) / putProject to add children through the proper API
        WorkflowJob branch1 = new WorkflowJob(multibranch, "master");
        branch1.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        addBranch(multibranch, branch1);

        WorkflowJob branch2 = new WorkflowJob(multibranch, "develop");
        branch2.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        addBranch(multibranch, branch2);

        multibranch.save();

        // Create a BuildMonitorView that includes the multibranch project (without recursion)
        var view = createBuildMonitorView(j, "Build Monitor").addJobs(multibranch);

        // Verify that the branch jobs appear in the monitor view
        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJob("MyMultibranch / master")
                .hasJob("MyMultibranch / develop");
    }

    /**
     * Adds a branch job to the multibranch project using the protected addLoadedChild method.
     * AbstractFolder (parent of WorkflowMultiBranchProject) exposes this for exactly this purpose.
     */
    private void addBranch(WorkflowMultiBranchProject parent, WorkflowJob child) throws Exception {
        // Walk up the class hierarchy to find addLoadedChild — it's in AbstractFolder
        Class<?> clazz = parent.getClass();
        Method addMethod = null;
        while (clazz != null && addMethod == null) {
            try {
                addMethod = clazz.getDeclaredMethod("addLoadedChild", Item.class);
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (addMethod == null) {
            throw new RuntimeException("Could not find addLoadedChild method in class hierarchy");
        }
        addMethod.setAccessible(true);
        addMethod.invoke(parent, child);
    }
}
