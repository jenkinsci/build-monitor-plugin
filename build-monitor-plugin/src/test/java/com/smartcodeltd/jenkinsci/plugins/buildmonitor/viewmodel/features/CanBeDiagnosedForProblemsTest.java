package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.junit.Test;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static hudson.model.Result.FAILURE;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class CanBeDiagnosedForProblemsTest {
    private JobView job;


    @Test
    public void should_describe_known_problems() {
        String rogueAi = "Pod bay doors didn't open";

        job = a(jobView().which(new CanBeDiagnosedForProblems()).of(
                a(job().whereTheLast(build().finishedWith(FAILURE).and().knownProblems(rogueAi)))));

        assertThat(diagnosedFailuresOf(job).value(), hasItem(rogueAi));
    }

    private CanBeDiagnosedForProblems.Problems diagnosedFailuresOf(JobView job) {
        return job.which(CanBeDiagnosedForProblems.class).asJson();
    }
}
