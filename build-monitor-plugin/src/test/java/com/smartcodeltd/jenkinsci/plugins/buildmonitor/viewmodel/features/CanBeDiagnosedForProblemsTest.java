package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config.BuildFailureAnalyzerDisplayedField;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static hudson.model.Result.FAILURE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class CanBeDiagnosedForProblemsTest {
    private JobView job;

    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @Before
    public void setup() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::getInstance).thenReturn(jenkins);
    }

    @After
    public void tearDown() {
        mockedJenkins.close();
    }

    @Test
    public void should_describe_known_problems() {
        String rogueAi = "Pod bay doors didn't open";

        job = a(jobView().which(new CanBeDiagnosedForProblems(BuildFailureAnalyzerDisplayedField.Name)).of(
                a(job().whereTheLast(build().finishedWith(FAILURE).and().knownProblems(rogueAi)))));

        assertThat(diagnosedFailuresOf(job).value(), hasItem(rogueAi));
    }

    private CanBeDiagnosedForProblems.Problems diagnosedFailuresOf(JobView job) {
        return job.which(CanBeDiagnosedForProblems.class).asJson();
    }
}
