package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config.BuildFailureAnalyzerDisplayedField;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Result;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class CanBeDiagnosedForProblemsTest {
    private JobView job;

    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @Before
    public void setup() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::get).thenReturn(jenkins);
    }

    @After
    public void tearDown() {
        mockedJenkins.close();
    }

    @Test
    public void should_describe_known_problems() {
        String rogueAi = "Pod bay doors didn't open";

        job = a(jobView()
                .which(new CanBeDiagnosedForProblems(BuildFailureAnalyzerDisplayedField.Name))
                .of(a(job().whereTheLast(
                                build().finishedWith(Result.FAILURE).and().knownProblems(rogueAi)))));

        assertThat(diagnosedFailuresOf(job).value(), hasItem(rogueAi));
    }

    private CanBeDiagnosedForProblems.Problems diagnosedFailuresOf(JobView job) {
        return job.which(CanBeDiagnosedForProblems.class).asJson();
    }
}
