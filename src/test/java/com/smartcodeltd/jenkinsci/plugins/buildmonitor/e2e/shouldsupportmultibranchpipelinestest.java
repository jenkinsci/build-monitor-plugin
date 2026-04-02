package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import com.microsoft.playwright.Page;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils;
import hudson.model.Job;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WithJenkins
class ShouldSupportMultibranchPipelinesTest {

    @Test
    void test(Page p, JenkinsRule j) throws Exception {
        // Create a multibranch pipeline project
        WorkflowMultiBranchProject multibranch = j.createProject(WorkflowMultiBranchProject.class, "MyMultibranch");

        // Create branch jobs and add them to the multibranch project
        WorkflowJob branch1 = new WorkflowJob(multibranch, "master");
        branch1.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } }", true));
        // Add branch via reflection (add method may be non-public)
        java.lang.reflect.Method addMethod = WorkflowMultiBranchProject.class.getDeclaredMethod("add", Job.class);
        addMethod.setAccessible(true);
        addMethod.invoke(multibranch, branch1);

        WorkflowJob branch2 = new WorkflowJob(multibranch, "develop");
        branch2.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } }", true));
        addMethod.invoke(multibranch, branch2);

        // Save the projects to persist state
        multibranch.save();
        branch1.save();
        branch2.save();

        // Create a BuildMonitorView that includes the multibranch project (without recursion)
        var view = BuildMonitorViewUtils.createBuildMonitorView(j, "Build Monitor")
                .addJobs(multibranch);

        // Verify that the branch jobs appear in the monitor view
        BuildMonitorViewPage.from(p, view.get())
                .goTo()
                .hasJob("MyMultibranch » master")
                .hasJob("MyMultibranch » develop");
    }
}
