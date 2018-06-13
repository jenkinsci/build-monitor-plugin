package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config.BuildFailureAnalyzerDisplayedField;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import jenkins.model.Jenkins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static hudson.model.Result.FAILURE;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class})
public class CanBeDiagnosedForProblemsTest {
    private JobView job;

    @Mock
    private Jenkins jenkins;

    @Before
    public void setup() {
        PowerMockito.mockStatic(Jenkins.class);
        PowerMockito.when(Jenkins.getInstance()).thenReturn(jenkins);
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
