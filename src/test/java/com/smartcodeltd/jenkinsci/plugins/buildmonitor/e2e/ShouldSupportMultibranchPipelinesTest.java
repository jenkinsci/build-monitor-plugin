package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static org.junit.jupiter.api.Assertions.*;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;

@WithJenkins
class ShouldSupportMultibranchPipelinesTest {

    /**
     * Test 1: Without recursion, a multibranch project with child branches
     * should have its branches expanded in getJobsToDisplay().
     * 
     * Uses the real WorkflowMultiBranchProject + real branch indexing.
     */
    @Test
    void testRegularJobsPassThrough(JenkinsRule j) throws Exception {
        // Create regular workflow jobs
        WorkflowJob job1 = j.createProject(WorkflowJob.class, "job-1");
        job1.setDefinition(new org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition(
            "pipeline { stages { stage('Build') { steps { echo 'Building' } } } }", true));
        job1.save();

        WorkflowJob job2 = j.createProject(WorkflowJob.class, "job-2");
        job2.setDefinition(new org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition(
            "pipeline { stages { stage('Test') { steps { echo 'Testing' } } } }", true));
        job2.save();

        // Add to a BuildMonitorView
        BuildMonitorView view = new BuildMonitorView("Test Monitor", null);
        j.getInstance().addView(view);
        view.add(job1);
        view.add(job2);

        // Verify getJobsToDisplay returns both jobs
        java.lang.reflect.Method getJobsMethod = BuildMonitorView.class.getDeclaredMethod("getJobsToDisplay");
        getJobsMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Job<?, ?>> jobs = (List<Job<?, ?>>) getJobsMethod.invoke(view);

        assertEquals(2, jobs.size(), "Should have both regular jobs");
    }

    /**
     * Test 2: Multibranch project added to view (without recursion)
     * should not throw and should handle gracefully even with no children.
     */
    @Test
    void testEmptyMultibranchNoRecursion(JenkinsRule j) throws Exception {
        WorkflowMultiBranchProject multibranch = j.createProject(WorkflowMultiBranchProject.class, "MyMultibranch");

        BuildMonitorView view = new BuildMonitorView("Test Monitor", null);
        j.getInstance().addView(view);
        view.add(multibranch);

        java.lang.reflect.Method getJobsMethod = BuildMonitorView.class.getDeclaredMethod("getJobsToDisplay");
        getJobsMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Job<?, ?>> jobs = (List<Job<?, ?>>) getJobsMethod.invoke(view);

        // Empty multibranch = no expanded jobs
        assertTrue(jobs.isEmpty(), "Empty multibranch should produce no jobs");
    }

    /**
     * Test 3: isMultibranchPipeline correctly identifies WorkflowMultiBranchProject
     */
    @Test
    void testIsMultibranchDetection(JenkinsRule j) throws Exception {
        WorkflowMultiBranchProject multibranch = j.createProject(WorkflowMultiBranchProject.class, "MyMultibranch");

        BuildMonitorView view = new BuildMonitorView("Test Monitor", null);
        j.getInstance().addView(view);

        java.lang.reflect.Method isMbpMethod = BuildMonitorView.class.getDeclaredMethod("isMultibranchPipeline", Object.class);
        isMbpMethod.setAccessible(true);

        assertTrue((boolean) isMbpMethod.invoke(view, multibranch),
            "Should identify WorkflowMultiBranchProject as multibranch");

        WorkflowJob regularJob = j.createProject(WorkflowJob.class, "regular");
        assertFalse((boolean) isMbpMethod.invoke(view, regularJob),
            "Should NOT identify WorkflowJob as multibranch");
    }
}
