package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import java.util.function.Supplier;

import org.jenkinsci.plugins.junitrealtimetestreporter.PipelineRealtimeTestResultAction;
import org.jenkinsci.plugins.junitrealtimetestreporter.TestProgress;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Beland
 */
public class RealtimeTestPluginRecipe implements Supplier<PipelineRealtimeTestResultAction> {
    private PipelineRealtimeTestResultAction realtimeTest;

    public RealtimeTestPluginRecipe() {
        realtimeTest = mock(PipelineRealtimeTestResultAction.class);
    }

    public RealtimeTestPluginRecipe withoutTestProgress() {
        when(realtimeTest.getTestProgress()).thenReturn(null);
        return this;
    }

    public RealtimeTestPluginRecipe withTestProgress(int expected, int completed, float expectedTime, float completedTime, String estimatedTime) {
        int completedPercent = Math.min((int) Math.floor((double) completed / (double) expected * 100d), 100);
        int completedTimePercent = Math.min((int) Math.floor((double) completedTime / (double) expectedTime * 100d), 100);
        
        TestProgress testProgress = mock(TestProgress.class);
        when(testProgress.getCompletedTests()).thenReturn(completed);
        when(testProgress.getCompletedTestsPercentage()).thenReturn(completedPercent);
        when(testProgress.getCompletedTime()).thenReturn(completedTime);
        when(testProgress.getCompletedTimePercentage()).thenReturn(completedTimePercent);
        when(testProgress.getEstimatedRemainingTime()).thenReturn(estimatedTime);
        when(testProgress.getExpectedTests()).thenReturn(expected);
        when(testProgress.getExpectedTime()).thenReturn(expectedTime);
        when(testProgress.getTestsLeftPercentage()).thenReturn(100 - completedPercent);
        when(testProgress.getTimeLeftPercentage()).thenReturn(100 - completedTimePercent);
        
        when(realtimeTest.getTestProgress()).thenReturn(testProgress);
        return this;
    }

    @Override
    public PipelineRealtimeTestResultAction get() {
        return realtimeTest;
    }
}
