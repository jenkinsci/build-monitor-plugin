package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.TopLevelItem;
import java.util.ArrayList;
import java.util.List;
import jenkins.branch.MultiBranchProject;
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

        // Create branch jobs and register them properly through the Jenkins API
        WorkflowJob branch1 = multibranch.createProject(WorkflowJob.class, "master");
        branch1.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        branch1.save();

        WorkflowJob branch2 = multibranch.createProject(WorkflowJob.class, "develop");
        branch2.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        branch2.save();

        multibranch.save();

        // Create a BuildMonitorView that includes the multibranch project (without recursion)
        var view = createBuildMonitorView(j, "Build Monitor").addJobs(multibranch);

        // Verify that the branch jobs appear in the monitor view
        BuildMonitorViewPage.from(p, view)
                .goTo()
                .hasJob("MyMultibranch / master")
                .hasJob("MyMultibranch / develop");
    }
}
