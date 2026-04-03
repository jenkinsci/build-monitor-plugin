package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static org.junit.jupiter.api.Assertions.*;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import hudson.model.Item;
import java.lang.reflect.Method;
import java.util.List;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class ShouldSupportMultibranchPipelinesTest {

    @Test
    void testMultibranchBranchesAppearWithoutRecursion(JenkinsRule j) throws Exception {
        // Create a multibranch pipeline project
        WorkflowMultiBranchProject multibranch = j.createProject(WorkflowMultiBranchProject.class, "MyMultibranch");

        // Add branch jobs via reflection (addLoadedChild in AbstractFolder)
        WorkflowJob branch1 = new WorkflowJob(multibranch, "master");
        branch1.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        addBranch(multibranch, branch1);

        WorkflowJob branch2 = new WorkflowJob(multibranch, "develop");
        branch2.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        addBranch(multibranch, branch2);

        multibranch.save();

        // Add multibranch to a BuildMonitorView (recursion OFF by default)
        BuildMonitorView view = new BuildMonitorView("Test Monitor", null);
        j.getInstance().addView(view);
        view.add(multibranch);

        // Verify getJobsToDisplay returns the branch jobs
        // Use reflection to call the private method
        Method getJobsMethod = BuildMonitorView.class.getDeclaredMethod("getJobsToDisplay");
        getJobsMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<?> jobs = (List<?>) getJobsMethod.invoke(view);

        // Should have 2 jobs (the branches), not the multibranch container itself
        assertEquals(2, jobs.size(), "Should expand multibranch into its branch jobs");

        // Verify the job names
        assertTrue(jobs.stream().anyMatch(job ->
                ((hudson.model.Job<?, ?>) job).getName().equals("master")));
        assertTrue(jobs.stream().anyMatch(job ->
                ((hudson.model.Job<?, ?>) job).getName().equals("develop")));
    }

    @Test
    void testMultibranchWithRecursionSkipsContainer(JenkinsRule j) throws Exception {
        // Create a multibranch pipeline project
        WorkflowMultiBranchProject multibranch = j.createProject(WorkflowMultiBranchProject.class, "MyMultibranch");

        WorkflowJob branch1 = new WorkflowJob(multibranch, "main");
        branch1.setDefinition(new CpsFlowDefinition("pipeline { stages { stage('Test') { steps { echo 'Hello' } } } }", true));
        addBranch(multibranch, branch1);
        multibranch.save();

        // Add to view WITH recursion enabled
        BuildMonitorView view = new BuildMonitorView("Test Monitor", null);
        j.getInstance().addView(view);
        view.add(multibranch);
        view.setRecurse(true);

        Method getJobsMethod = BuildMonitorView.class.getDeclaredMethod("getJobsToDisplay");
        getJobsMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<?> jobs = (List<?>) getJobsMethod.invoke(view);

        // With recursion, the multibranch container (Job instance) should be skipped
        // Children are discovered by Jenkins view recursion, not by our expansion
        assertTrue(jobs.isEmpty(), "With recursion, multibranch container should be skipped (children discovered by Jenkins)");
    }

    /**
     * Adds a branch job to the multibranch project via reflection.
     */
    private void addBranch(WorkflowMultiBranchProject parent, WorkflowJob child) throws Exception {
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
